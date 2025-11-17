package trabajo.integrador.dao;

import trabajo.integrador.entities.Rol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ramiromoralesdev
 */
public class MysqlRolDAO implements DAO<Rol, Integer> {

    private Connection conn;

    public MysqlRolDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void crear(Rol rol) {
        String sql = "INSERT INTO rol (nombre, descripcion) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rol.getNombre());
            ps.setString(2, rol.getDescripcion());
            ps.executeUpdate();
            System.out.println("Rol creado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear rol: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Rol rol) {
        String sql = "UPDATE rol SET nombre = ?, descripcion = ? WHERE id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rol.getNombre());
            ps.setString(2, rol.getDescripcion());
            ps.setInt(3, rol.getId());
            
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Rol actualizado correctamente. " + rol);
            } else {
                System.out.println("No se encontró el rol con ID: " + rol.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar rol: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM rol WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Rol eliminado: " + id);
            } else {
                System.out.println("No se encontró el rol con ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar rol: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Rol leerPorId(Integer id) {
        String sql = "SELECT * FROM rol WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Rol(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al leer rol: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Rol> leerTodos() {
        List<Rol> lista = new ArrayList<>();
        String sql = "SELECT * FROM rol";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Rol(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al leer roles: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
}

