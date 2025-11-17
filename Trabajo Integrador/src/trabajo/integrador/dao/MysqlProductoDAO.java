package trabajo.integrador.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import trabajo.integrador.entities.Producto;

public class MysqlProductoDAO implements DAO<Producto, Integer> {

    private Connection conn;

    public MysqlProductoDAO(Connection conn) {
        this.conn = conn;
    }

@Override
public void crear(Producto producto) {
    // ✅ VALIDACIONES BÁSICAS PREVIAS
    if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
        System.err.println("❌ Error: El nombre del producto no puede estar vacío.");
        return;
    }

    if (producto.getPrecio() <= 0) {
        System.err.println("❌ Error: El precio debe ser mayor a cero.");
        return;
    }

    if (producto.getPeso() <= 0) {
        System.err.println("❌ Error: El peso debe ser mayor a cero.");
        return;
    }

    // ✅ Sentencia SQL segura con PreparedStatement
    String sql = "INSERT INTO producto (nombre, marca, categoria, precio, peso) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, producto.getNombre());
        ps.setString(2, producto.getMarca());
        ps.setString(3, producto.getCategoria());
        ps.setDouble(4, producto.getPrecio());
        ps.setDouble(5, producto.getPeso());
        ps.executeUpdate();

        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int idGenerado = generatedKeys.getInt(1);
                producto.setId(idGenerado);
                System.out.println("✅ Producto creado correctamente con ID: " + idGenerado);
            } else {
                System.out.println("Producto creado correctamente, pero no se pudo obtener el ID generado.");
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al crear producto: " + e.getMessage());
        e.printStackTrace();
    }
}


    @Override
public void actualizar(Producto producto) {
    String sql = "UPDATE producto SET nombre = ?, precio = ?, marca = ?, categoria = ?, peso = ? WHERE id = ?";
    
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, producto.getNombre());
        ps.setDouble(2, producto.getPrecio());
        ps.setString(3, producto.getMarca());
        ps.setString(4, producto.getCategoria());
        ps.setDouble(5, producto.getPeso());
        ps.setInt(6, producto.getId());
        
        int filas = ps.executeUpdate();
        if (filas > 0) {
            System.out.println("Producto actualizado correctamente. " + producto);
        } else {
            System.out.println("No se encontró el producto con ID: " + producto.getId());
        }
    } catch (SQLException e) {
        System.err.println("Error al actualizar el producto: " + e.getMessage());
        e.printStackTrace();
    }
}


    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM producto WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Producto eliminado: " + id);
            } else {
                System.out.println("No se encontró el producto con ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Producto leerPorId(Integer id) {
    // Use INNER JOIN to fetch the codigo_barras.id for the producto (only products with a code will be returned)
    String sql = "SELECT p.*, cb.id AS codigo_barras_id FROM producto p INNER JOIN codigo_barras cb ON cb.producto_id = p.id WHERE p.id = ? AND p.eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Build Producto first, then set codigo_id explicitly from ResultSet
                Producto producto = new Producto(
                    rs.getInt("id"),
                    rs.getBoolean("eliminado"),
                    rs.getString("nombre"),
                    rs.getString("marca"),
                    rs.getString("categoria"),
                    rs.getDouble("precio"),
                    rs.getDouble("peso"),
                    rs.getTimestamp("fecha_creacion") != null ? new Date(rs.getTimestamp("fecha_creacion").getTime()) : null,
                    0
                );

                Object obj = rs.getObject("codigo_barras_id");
                if (obj != null) {
                    producto.setCodigoId(rs.getInt("codigo_barras_id"));
                }
                return producto;

            }
        } catch (SQLException e) {
            System.err.println("Error al leer producto: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Producto> leerTodos() {
        List<Producto> lista = new ArrayList<>();
    // Use INNER JOIN to fetch productos that have a codigo_barras (join on producto_id)
    String sql = "SELECT p.*, cb.id AS codigo_barras_id FROM producto p INNER JOIN codigo_barras cb ON cb.producto_id = p.id";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Build Producto without codigo_id then set it from ResultSet
                Producto producto = new Producto(
                    rs.getInt("id"),
                    rs.getBoolean("eliminado"),
                    rs.getString("nombre"),
                    rs.getString("marca"),
                    rs.getString("categoria"),
                    rs.getDouble("precio"),
                    rs.getDouble("peso"),
                    rs.getTimestamp("fecha_creacion") != null ? new Date(rs.getTimestamp("fecha_creacion").getTime()) : null,
                    0
                );
                Object obj = rs.getObject("codigo_barras_id");
                if (obj != null) {
                    producto.setCodigoId(rs.getInt("codigo_barras_id"));
                }
                
                
                lista.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer productos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
}
