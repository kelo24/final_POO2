package models;

public enum EstadoPedido {
    PENDIENTE("PENDIENTE"),
    CONFIRMADO("CONFIRMADO"),
    CONFIRMADO_SIN_ADELANTO("CONFIRMADO SIN ADELANTO"),
    CANCELADO("CANCELADO");
    
    private final String descripcion;
    
    EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}