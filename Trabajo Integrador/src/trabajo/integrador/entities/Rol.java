package trabajo.integrador.entities;

import java.util.ArrayList;
import java.util.List;

public class Rol {
    private int id;
    private String nombre;
    private String descripcion;
    private List<String> permisos;

    public Rol(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.permisos = new ArrayList<>();
    }
    
    // Constructor para crear nuevo (sin ID)
    public Rol(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.permisos = new ArrayList<>();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getPermisos() {
        if (permisos == null) {
            permisos = new ArrayList<>();
        }
        // Retornar una copia para evitar modificaciones externas no controladas
        return new ArrayList<>(permisos);
    }

  
    public void asignarPermisos(List<String> permisos) {
        if (permisos == null) {
            this.permisos = new ArrayList<>();
        } else {
            // Crear una copia para evitar referencias externas
            this.permisos = new ArrayList<>(permisos);
        }
    }
    

    public void agregarPermiso(String permiso) {
        if (permisos == null) {
            permisos = new ArrayList<>();
        }
        if (permiso != null && !permiso.trim().isEmpty() && !permisos.contains(permiso)) {
            permisos.add(permiso);
        }
    }
    

    public boolean eliminarPermiso(String permiso) {
        if (permisos == null) {
            return false;
        }
        return permisos.remove(permiso);
    }
    
    public boolean tienePermiso(String permiso) {
        if (permisos == null || permiso == null) {
            return false;
        }
        return permisos.contains(permiso);
    }

    @Override
    public String toString() {
        return "Rol{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + '}';
    }
}
