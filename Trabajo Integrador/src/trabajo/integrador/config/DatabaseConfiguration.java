package trabajo.integrador.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration {
    private static final String URL = "jdbc:mysql://localhost:3306/db_producto";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    // Método estático para obtener conexión
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("❌ Driver JDBC no encontrado.", e);
        }
    }
    
    // Método opcional para probar la conexión
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar: " + e.getMessage());
            
        }
    }
}