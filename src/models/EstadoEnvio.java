package models;

public enum EstadoEnvio {
    SIN_REGISTRO("SIN REGISTRO"),
    REGISTRADO("REGISTRADO"),
    EN_ORIGEN("EN ORIGEN"),
    EN_TRANSITO("EN TRANSITO"),
    EN_DESTINO("EN DESTINO"),
    ENTREGADO("ENTREGADO");
    
    private final String descripcion;
    
    EstadoEnvio(String descripcion) {
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