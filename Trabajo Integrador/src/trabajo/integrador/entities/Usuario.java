package trabajo.integrador.entities;

import trabajo.integrador.dao.MysqlRolDAO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;

public class Usuario {
    private int id;
    private boolean eliminado;
    private String username;
    private String email;
    private String passwordHash;
    private int rolId;
    private Boolean activo;
    private Date fechaRegistro;

    // Constructor completo
    public Usuario(int id, boolean eliminado, String username, String email, String passwordHash, int rolId, Boolean activo, Date fechaRegistro) {
        this.id = id;
        this.eliminado = eliminado;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rolId = rolId;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
    }
    
    // Constructor para crear nuevo (sin ID)
    public Usuario(String username, String email, String passwordHash, int rolId) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rolId = rolId;
        this.activo = true;
        this.eliminado = false;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        this.rolId = rolId;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }


    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            
            // Convertir bytes a hexadecimal
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error al generar hash: " + e.getMessage());
            return null;
        }
    }


    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        
        // Verificar que el usuario esté activo
        if (activo == null || !activo) {
            return false;
        }
        
        // Verificar username
        if (!this.username.equals(username)) {
            return false;
        }
        
        // Hashear la contraseña ingresada y comparar con el hash guardado
        String passwordHashIngresado = hashPassword(password);
        return passwordHashIngresado != null && this.passwordHash.equals(passwordHashIngresado);
    }


    public void cambiarPassword(String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            System.err.println("Error: La contraseña no puede estar vacía");
            return;
        }
        
        // Hashear la contraseña antes de guardarla
        String hashedPassword = hashPassword(newPassword);
        if (hashedPassword != null) {
            this.passwordHash = hashedPassword;
        } else {
            System.err.println("Error: No se pudo hashear la contraseña");
        }
    }

    public void activarUsuario() {
        this.activo = true;
    }

    public void desactivarUsuario() {
        this.activo = false;
    }

 
    public Rol obtenerRol(MysqlRolDAO rolDAO) {
        if (rolDAO == null) {
            System.err.println("Error: El DAO de roles no puede ser null");
            return null;
        }
        
        try {
            return rolDAO.leerPorId(this.rolId);
        } catch (Exception e) {
            System.err.println("Error al obtener el rol: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", username=" + username + ", email=" + email + ", rolId=" + rolId + ", activo=" + activo + ", fechaRegistro=" + fechaRegistro + '}';
    }
}
