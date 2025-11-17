package trabajo.integrador.services;

import java.sql.Connection;
import java.sql.SQLException;
import trabajo.integrador.dao.MysqlUsuarioDAO;
import trabajo.integrador.entities.Usuario;

/**
 * Servicio transaccional para operaciones de Usuario
 * Garantiza atomicidad en operaciones que requieren múltiples queries
 */
public class UsuarioRolTransaccion {
    
    private Connection conn;
    private MysqlUsuarioDAO usuarioDAO;

    public UsuarioRolTransaccion(Connection conn) {
        this.conn = conn;
        this.usuarioDAO = new MysqlUsuarioDAO(conn);
    }

    /**
     * Crea un usuario de forma transaccional
     * Si falla, se hace rollback automático
     */
    public void crearUsuarioTransaccional(Usuario usuario) throws SQLException {
        try {
            conn.setAutoCommit(false); // Iniciar transacción
            
            usuarioDAO.crear(usuario);
            
            conn.commit(); // Confirmar cambios
            System.out.println("Usuario creado transaccionalmente.");
        } catch (SQLException e) {
            conn.rollback(); // Revertir cambios en caso de error
            System.err.println("Error en transacción. Rollback realizado: " + e.getMessage());
            throw e;
        } finally {
            conn.setAutoCommit(true); // Restaurar auto-commit
        }
    }


    public void actualizarUsuarioTransaccional(Usuario usuario) throws SQLException {
        try {
            conn.setAutoCommit(false); // Iniciar transacción
            
            usuarioDAO.actualizar(usuario);
            
            conn.commit(); // Confirmar cambios
            System.out.println("Usuario actualizado transaccionalmente.");
        } catch (SQLException e) {
            conn.rollback(); // Revertir cambios en caso de error
            System.err.println("Error en transacción. Rollback realizado: " + e.getMessage());
            throw e;
        } finally {
            conn.setAutoCommit(true); // Restaurar auto-commit
        }
    }


    public void eliminarUsuarioTransaccional(Integer id) throws SQLException {
        try {
            conn.setAutoCommit(false); // Iniciar transacción
            
            usuarioDAO.eliminar(id);
            
            conn.commit(); // Confirmar cambios
            System.out.println("Usuario eliminado transaccionalmente.");
        } catch (SQLException e) {
            conn.rollback(); // Revertir cambios en caso de error
            System.err.println("Error en transacción. Rollback realizado: " + e.getMessage());
            throw e;
        } finally {
            conn.setAutoCommit(true); // Restaurar auto-commit
        }
    }
}