package trabajo.integrador.services;

import java.sql.Connection;
import java.sql.SQLException;
import trabajo.integrador.dao.MysqlCodigoBarrasDAO;
import trabajo.integrador.entities.CodigoBarras;

/**
 * Servicio transaccional para operaciones de CodigoBarras
 * Garantiza atomicidad en operaciones que requieren verificaciones previas
 */
public class CodigoBarrasTransaccion {
    
    private Connection conn;
    private MysqlCodigoBarrasDAO codigoBarrasDAO;

    public CodigoBarrasTransaccion(Connection conn) {
        this.conn = conn;
        this.codigoBarrasDAO = new MysqlCodigoBarrasDAO(conn);
    }

    
    public void actualizarCodigoBarrasTransaccional(CodigoBarras codigoBarras) throws SQLException {
        try {
            conn.setAutoCommit(false); // Iniciar transacción
            
            codigoBarrasDAO.actualizar(codigoBarras);
            
            conn.commit(); // Confirmar cambios
            System.out.println("Código de barras actualizado transaccionalmente.");
        } catch (SQLException e) {
            conn.rollback(); // Revertir cambios en caso de error
            System.err.println("Error en transacción. Rollback realizado: " + e.getMessage());
            throw e;
        } finally {
            conn.setAutoCommit(true); // Restaurar auto-commit
        }
    }

    /**
     * Crea un código de barras de forma transaccional
     */
    public void crearCodigoBarrasTransaccional(CodigoBarras codigoBarras) throws SQLException {
        try {
            conn.setAutoCommit(false); // Iniciar transacción
            
            codigoBarrasDAO.crear(codigoBarras);
            
            conn.commit(); // Confirmar cambios
            System.out.println("Código de barras creado transaccionalmente.");
        } catch (SQLException e) {
            conn.rollback(); // Revertir cambios en caso de error
            System.err.println("Error en transacción. Rollback realizado: " + e.getMessage());
            throw e;
        } finally {
            conn.setAutoCommit(true); // Restaurar auto-commit
        }
    }

    
    public void eliminarCodigoBarrasTransaccional(Integer id) throws SQLException {
        try {
            conn.setAutoCommit(false); // Iniciar transacción
            
            codigoBarrasDAO.eliminar(id);
            
            conn.commit(); // Confirmar cambios
            System.out.println("✅ Código de barras eliminado transaccionalmente.");
        } catch (SQLException e) {
            conn.rollback(); // Revertir cambios en caso de error
            System.err.println("❌ Error en transacción. Rollback realizado: " + e.getMessage());
            throw e;
        } finally {
            conn.setAutoCommit(true); // Restaurar auto-commit
        }
    }
}

