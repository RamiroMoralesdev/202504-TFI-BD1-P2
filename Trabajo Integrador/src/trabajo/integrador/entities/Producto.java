package trabajo.integrador.entities;

import java.sql.Date;

public class Producto{
    private int id;
    private boolean eliminado;
    private String nombre;
    private String marca;
    private String categoria;
    private double precio;
    private double peso;
    private Date fechaCreacion;
    private int codigo_id;
    private String codigo_barras;
    
    public Producto(int id, String nombre, String marca, double precio, double peso) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.peso = peso;
    }
    
    public Producto(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public Producto(int id, boolean eliminado, String nombre, String marca, String categoria, double precio, double peso, Date fechaCreacion, int codigo_id) {
        this.id = id;
        this.eliminado = eliminado;
        this.nombre = nombre;
        this.marca = marca;
        this.categoria = categoria;
        this.precio = precio;
        this.peso = peso;
        this.fechaCreacion = fechaCreacion;
        this.codigo_id = codigo_id;
        
    }

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public int getCodigoId() {
        return this.codigo_id;
    }

    public void setCodigoId(int codigo_id) {
        this.codigo_id = codigo_id;
    }
    
    

    public double getPrecioConIva(double iva) {
        return this.precio * (1 + iva);
    }

    public double getPesoEnKilos() { // El peso se guarda en gramos?
        return this.peso / 1000;
    }

    public boolean validarPrecio() {
        return this.precio > 0;
    }
    
    // Este método necesita acceso al DAO para obtener el código de barras
    // Se debe usar MysqlCodigoBarrasDAO.leerPorId() o buscar por producto_id
    // public CodigoBarras obtenerCodigoBarras() {
    //     // Requiere acceso al DAO, no se puede implementar aquí sin conexión
    //     return null;
    // }

    @Override
    public String toString() {
        return "Producto{" + "id=" + id + ", eliminado=" + eliminado + ", nombre=" + nombre + ", marca=" + marca + ", categoria=" + categoria + ", precio=" + precio + ", peso=" + peso + ", fechaCreacion=" + fechaCreacion + ", codigo_barras_id=" + codigo_id + '}';
    }
    
    
}
