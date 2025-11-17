package trabajo.integrador.services;

import java.sql.Connection;
import java.sql.SQLException;
import trabajo.integrador.dao.MysqlProductoDAO;
import trabajo.integrador.dao.MysqlCodigoBarrasDAO;
import trabajo.integrador.entities.Producto;
import trabajo.integrador.entities.CodigoBarras;

/**
 * Servicio transaccional para operaciones que involucran Producto y CodigoBarras
 * Garantiza que ambas entidades se creen/actualicen de forma atómica
 */
public class ProductoCodigoBarrasTransaccion {
    
    private Connection conn;
    private MysqlProductoDAO productoDAO;
    private MysqlCodigoBarrasDAO codigoBarrasDAO;

    public ProductoCodigoBarrasTransaccion(Connection conn) {
        this.conn = conn;
        this.productoDAO = new MysqlProductoDAO(conn);
        this.codigoBarrasDAO = new MysqlCodigoBarrasDAO(conn);
    }

    /**
     * Crea un producto junto con su código de barras de forma transaccional
     * Si falla cualquiera de las dos operaciones, se hace rollback completo
     */
    public void crearProductoConCodigoBarras(Producto producto, CodigoBarras codigoBarras) throws SQLException {
        try {
            conn.setAutoCommit(false); // Iniciar transacción
            
            // 1. Crear el producto
            productoDAO.crear(producto);
            
            // 2. Asignar el ID generado del producto al código de barras
            codigoBarras.setProductoId(producto.getId());
            
            // 3. Crear el código de barras
            codigoBarrasDAO.crear(codigoBarras);
            
            conn.commit(); // Confirmar cambios
            System.out.println("✅ Producto y código de barras creados transaccionalmente.");
        } catch (SQLException e) {
            conn.rollback(); // Revertir cambios en caso de error
            System.err.println("❌ Error en transacción. Rollback realizado: " + e.getMessage());
            throw e;
        } finally {
            conn.setAutoCommit(true); // Restaurar auto-commit
        }
    }

    /**
     * Actualiza un producto y su código de barras de forma transaccional
     */
    public void actualizarProductoConCodigoBarras(Producto producto, CodigoBarras codigoBarras) throws SQLException {
        try {
            conn.setAutoCommit(false); // Iniciar transacción
            
            // 1. Actualizar el producto
            productoDAO.actualizar(producto);
            
            // 2. Actualizar el código de barras
            codigoBarrasDAO.actualizar(codigoBarras);
            
            conn.commit(); // Confirmar cambios
            System.out.println("✅ Producto y código de barras actualizados transaccionalmente.");
        } catch (SQLException e) {
            conn.rollback(); // Revertir cambios en caso de error
            System.err.println("❌ Error en transacción. Rollback realizado: " + e.getMessage());
            throw e;
        } finally {
            conn.setAutoCommit(true); // Restaurar auto-commit
        }
    }

    /**
     * Elimina un producto y su código de barras de forma transaccional
     * Nota: El CASCADE en la BD eliminará automáticamente el código de barras,
     * pero esta transacción garantiza atomicidad
     */
    public void eliminarProductoConCodigoBarras(Integer productoId) throws SQLException {
        try {
            conn.setAutoCommit(false); // Iniciar transacción
            
            // 1. Buscar el código de barras asociado
            CodigoBarras codigoBarras = codigoBarrasDAO.leerPorId(productoId);
            if (codigoBarras != null) {
                codigoBarrasDAO.eliminar(codigoBarras.getId());
            }
            
            // 2. Eliminar el producto
            productoDAO.eliminar(productoId);
            
            conn.commit(); // Confirmar cambios
            System.out.println("✅ Producto y código de barras eliminados transaccionalmente.");
        } catch (SQLException e) {
            conn.rollback(); // Revertir cambios en caso de error
            System.err.println("❌ Error en transacción. Rollback realizado: " + e.getMessage());
            throw e;
        } finally {
            conn.setAutoCommit(true); // Restaurar auto-commit
        }
    }
}

