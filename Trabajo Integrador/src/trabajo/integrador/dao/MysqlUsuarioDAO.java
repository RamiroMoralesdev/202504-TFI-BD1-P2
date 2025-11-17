package trabajo.integrador.dao;

import trabajo.integrador.entities.Usuario;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ramiromoralesdev
 */
public class MysqlUsuarioDAO implements DAO<Usuario, Integer> {

    private Connection conn;

    public MysqlUsuarioDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void crear(Usuario usuario) {
        String sql = "INSERT INTO usuario (username, email, password_hash, rol_id, activo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getPasswordHash());
            ps.setInt(4, usuario.getRolId());
            ps.setBoolean(5, usuario.getActivo() != null ? usuario.getActivo() : true);
            ps.executeUpdate();
            System.out.println("Usuario creado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Usuario usuario) {
        // Verificar si el username ya está en uso por otro usuario
        String checkUsernameSql = "SELECT id FROM usuario WHERE username = ? AND id != ?";
        try (PreparedStatement checkPs = conn.prepareStatement(checkUsernameSql)) {
            checkPs.setString(1, usuario.getUsername());
            checkPs.setInt(2, usuario.getId());
            ResultSet rs = checkPs.executeQuery();
            if (rs.next()) {
                System.err.println("Error: El username '" + usuario.getUsername() + "' ya está asignado a otro usuario (ID: " + rs.getInt("id") + ")");
                System.err.println("   No se puede actualizar porque username tiene restricción UNIQUE.");
                return;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar username: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        // Verificar si el email ya está en uso por otro usuario
        String checkEmailSql = "SELECT id FROM usuario WHERE email = ? AND id != ?";
        try (PreparedStatement checkPs = conn.prepareStatement(checkEmailSql)) {
            checkPs.setString(1, usuario.getEmail());
            checkPs.setInt(2, usuario.getId());
            ResultSet rs = checkPs.executeQuery();
            if (rs.next()) {
                System.err.println("Error: El email '" + usuario.getEmail() + "' ya está asignado a otro usuario (ID: " + rs.getInt("id") + ")");
                System.err.println("   No se puede actualizar porque email tiene restricción UNIQUE.");
                return;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar email: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        // Si username y email están libres, proceder con la actualización
        String sql = "UPDATE usuario SET username = ?, email = ?, password_hash = ?, rol_id = ?, activo = ? WHERE id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getPasswordHash());
            ps.setInt(4, usuario.getRolId());
            ps.setBoolean(5, usuario.getActivo() != null ? usuario.getActivo() : true);
            ps.setInt(6, usuario.getId());
            
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Usuario actualizado correctamente. " + usuario);
            } else {
                System.out.println("No se encontró el usuario con ID: " + usuario.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Usuario eliminado: " + id);
            } else {
                System.out.println("No se encontró el usuario con ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Usuario leerPorId(Integer id) {
        String sql = "SELECT * FROM usuario WHERE id = ? AND eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id"),
                    rs.getBoolean("eliminado"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    rs.getInt("rol_id"),
                    rs.getBoolean("activo"),
                    rs.getTimestamp("fecha_registro") != null ? new Date(rs.getTimestamp("fecha_registro").getTime()) : null
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al leer usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Usuario> leerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Usuario(
                    rs.getInt("id"),
                    rs.getBoolean("eliminado"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    rs.getInt("rol_id"),
                    rs.getBoolean("activo"),
                    rs.getTimestamp("fecha_registro") != null ? new Date(rs.getTimestamp("fecha_registro").getTime()) : null
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al leer usuarios: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
}

