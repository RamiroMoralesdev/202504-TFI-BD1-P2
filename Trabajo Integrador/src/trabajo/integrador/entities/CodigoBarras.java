package trabajo.integrador.entities;

import trabajo.integrador.dao.tipoCodigoBarras;

import java.sql.Date;

public class CodigoBarras {
    private int id;
    private boolean eliminado;
    private int productoId;
    private tipoCodigoBarras tipo;
    private String valor;
    private Date fechaAsignacion; 
    private String observaciones;
    
    // Constructor completo
    public CodigoBarras(int id, int productoId, String tipo, String valor, Date fechaAsignacion, String observaciones) {
        this.id = id;
        this.productoId = productoId;
        this.tipo = tipoCodigoBarras.valueOf(tipo.toUpperCase());
        this.valor = valor;
        this.fechaAsignacion = fechaAsignacion;
        this.observaciones = observaciones;
    }
    
    // Constructor para crear nuevo (sin ID)
    public CodigoBarras(int productoId, tipoCodigoBarras tipo, String valor, Date fechaAsignacion, String observaciones) {
        this.productoId = productoId;
        this.tipo = tipo;
        this.valor = valor;
        this.fechaAsignacion = fechaAsignacion;
        this.observaciones = observaciones;
    }

    // Getters
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

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public tipoCodigoBarras getTipo() {
        return tipo;
    }

    public void setTipo(tipoCodigoBarras tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean validarFormato() {
        if (valor == null || valor.isEmpty()) {
            return false;
        }
        
        // Verificar que solo contenga dígitos
        if (!valor.matches("\\d+")) {
            return false;
        }
        
        // Validar longitud según el tipo
        switch (tipo) {
            case EAN13:
                return valor.length() == 13;
            case EAN8:
                return valor.length() == 8;
            case UPC:
                return valor.length() == 12;
            default:
                return false;
        }
    }


    public String generarDigitoVerificador() {
        if (valor == null || valor.isEmpty()) {
            return "0";
        }
        
        // Obtener los dígitos sin el último (si existe)
        String codigoSinDigito;
        int longitudEsperada;
        
        switch (tipo) {
            case EAN13:
                longitudEsperada = 13;
                codigoSinDigito = valor.length() >= 12 ? valor.substring(0, 12) : valor;
                break;
            case EAN8:
                longitudEsperada = 8;
                codigoSinDigito = valor.length() >= 7 ? valor.substring(0, 7) : valor;
                break;
            case UPC:
                longitudEsperada = 12;
                codigoSinDigito = valor.length() >= 11 ? valor.substring(0, 11) : valor;
                break;
            default:
                return "0";
        }
        
        // Si el código ya tiene la longitud completa, usar todos excepto el último
        if (codigoSinDigito.length() == longitudEsperada - 1) {
            // Calcular dígito verificador
            int suma = 0;
            for (int i = 0; i < codigoSinDigito.length(); i++) {
                int digito = Character.getNumericValue(codigoSinDigito.charAt(i));
            
                // índice 0 = posición 1 (impar) -> x1
                // índice 1 = posición 2 (par) -> x3
                if (i % 2 == 0) {
                    suma += digito * 1;
                } else {
                    suma += digito * 3;
                }
            }
            
            // Calcular dígito verificador: (10 - (suma % 10)) % 10
            int digitoVerificador = (10 - (suma % 10)) % 10;
            return String.valueOf(digitoVerificador);
        }
        
        // Si el código no tiene la longitud correcta, retornar 0
        return "0";
    }

    @Override
    public String toString() {
        return "CodigoBarras{" + "id=" + id + ", productoId=" + productoId + ", tipo=" + tipo + ", valor=" + valor + ", fechaAsignacion=" + fechaAsignacion + ", observaciones=" + observaciones + '}';
    }
}