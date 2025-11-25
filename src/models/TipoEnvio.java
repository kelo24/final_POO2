package models;

import strategy.*;

/**
 * Enum para tipos de envío con Strategy integrado
 */
public enum TipoEnvio {
    NORMAL("Normal", new EnvioNormal()),
    EXPRESS("Express", new EnvioExpress()),
    PRIORITARIO("Prioritario", new EnvioPrioritario());
    
    private final String descripcion;
    private final EstrategiaEnvio estrategia;
    
    TipoEnvio(String descripcion, EstrategiaEnvio estrategia) {
        this.descripcion = descripcion;
        this.estrategia = estrategia;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public EstrategiaEnvio getEstrategia() {
        return estrategia;
    }
    
    /**
     * Obtiene el TipoEnvio a partir de su descripción
     */
    public static TipoEnvio fromDescripcion(String descripcion) {
        if (descripcion == null) return NORMAL;
        
        for (TipoEnvio tipo : values()) {
            if (tipo.descripcion.equalsIgnoreCase(descripcion)) {
                return tipo;
            }
        }
        return NORMAL; // Por defecto
    }
    
    /**
     * Calcula el costo de envío para un pedido
     */
    public double calcularCosto(models.Pedido pedido) {
        return estrategia.calcularCosto(pedido);
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}