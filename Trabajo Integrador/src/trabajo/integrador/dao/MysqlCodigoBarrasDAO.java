/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabajo.integrador.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import trabajo.integrador.entities.CodigoBarras;
import trabajo.integrador.dao.tipoCodigoBarras;

/**
 *
 * @author ramiromoralesdev
 */
public class MysqlCodigoBarrasDAO implements DAO<CodigoBarras, Integer> { 

    private Connection conn;

    public MysqlCodigoBarrasDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void crear(CodigoBarras c) {
        String sql = "INSERT INTO codigo_barras (producto_id, tipo, valor, fecha_asignacion, observaciones) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getProductoId());
            ps.setString(2, c.getTipo().toString());
            ps.setString(3, c.getValor());
            ps.setDate(4, c.getFechaAsignacion() != null ? c.getFechaAsignacion() : new Date(System.currentTimeMillis()));
            ps.setString(5, c.getObservaciones());
            ps.executeUpdate();
            System.out.println("✅ Código de barras creado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al crear código de barras: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(CodigoBarras c) {
        // Verificar si el producto_id ya está en uso por otro código de barras
        String checkSql = "SELECT id FROM codigo_barras WHERE producto_id = ? AND id != ?";
        try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setInt(1, c.getProductoId());
            checkPs.setInt(2, c.getId());
            ResultSet rs = checkPs.executeQuery();
            if (rs.next()) {
                System.err.println("Error: El producto_id " + c.getProductoId() + " ya está asignado a otro código de barras (ID: " + rs.getInt("id") + ")");
                System.err.println("   No se puede actualizar porque producto_id tiene restricción UNIQUE.");
                return;
            }
        } catch (SQLException e) {
            System.err.println(" Error al verificar producto_id: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        // Si el producto_id está libre, proceder con la actualización
        String sql = "UPDATE codigo_barras SET producto_id = ?, tipo = ?, valor = ?, fecha_asignacion = ?, observaciones = ? WHERE id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getProductoId());
            ps.setString(2, c.getTipo().toString());
            ps.setString(3, c.getValor());
            ps.setDate(4, c.getFechaAsignacion());
            ps.setString(5, c.getObservaciones());
            ps.setInt(6, c.getId());
            
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Código de barras actualizado correctamente. " + c);
            } else {
                System.out.println("No se encontró el código de barras con ID: " + c.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar código de barras: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM codigo_barras WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Código de barras eliminado: " + id);
            } else {
                System.out.println("No se encontró el código de barras con ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar código de barras: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public CodigoBarras leerPorId(Integer id) {
        String sql = "SELECT * FROM codigo_barras WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CodigoBarras(
                    rs.getInt("id"),
                    rs.getInt("producto_id"),
                    rs.getString("tipo"),
                    rs.getString("valor"),
                    rs.getDate("fecha_asignacion"),
                    rs.getString("observaciones")
                );
            }
        } catch (SQLException e) {
            System.err.println(" Error al leer código de barras: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CodigoBarras> leerTodos() {
        List<CodigoBarras> lista = new ArrayList<>();
        String sql = "SELECT * FROM codigo_barras";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new CodigoBarras(
                    rs.getInt("id"),
                    rs.getInt("producto_id"),
                    rs.getString("tipo"),
                    rs.getString("valor"),
                    rs.getDate("fecha_asignacion"),
                    rs.getString("observaciones")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al leer códigos de barras: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
}
    