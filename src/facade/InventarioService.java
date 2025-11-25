package facade;

import models.Producto;

/**
 * InventarioService - Servicio para gestión de inventario
 */
public class InventarioService {

    /**
     * Descuenta stock de un producto
     * @return true si se pudo descontar, false si no hay stock suficiente
     */
    public boolean descontarStock(Producto p, int cantidad) {
        if (p == null) {
            System.err.println("Error: Producto nulo");
            return false;
        }
        
        if (p.getStock() < cantidad) {
            System.err.println("Stock insuficiente: " + p.getStock() + " < " + cantidad);
            return false;
        }
        
        p.setStock(p.getStock() - cantidad);
        System.out.println("Stock descontado: " + cantidad + " de " + p.getNombre());
        return true;
    }

    /**
     * Repone stock de un producto
     */
    public void reponerStock(Producto p, int cantidad) {
        if (p == null) {
            System.err.println("Error: Producto nulo");
            return;
        }
        
        p.setStock(p.getStock() + cantidad);
        System.out.println("Stock repuesto: " + cantidad + " de " + p.getNombre());
    }
    
    /**
     * Valida si hay stock suficiente
     */
    public boolean validarStock(Producto p, int cantidad) {
        if (p == null) return false;
        return p.getStock() >= cantidad;
    }
    
    /**
     * Obtiene el stock actual de un producto
     */
    public int obtenerStock(Producto p) {
        if (p == null) return 0;
        return p.getStock();
    }
    
    /**
     * Ajusta el stock de un producto (puede ser positivo o negativo)
     */
    public boolean ajustarStock(Producto p, int ajuste) {
        if (p == null) return false;
        
        int nuevoStock = p.getStock() + ajuste;
        
        if (nuevoStock < 0) {
            System.err.println("Error: El ajuste resultaría en stock negativo");
            return false;
        }
        
        p.setStock(nuevoStock);
        System.out.println("Stock ajustado: " + ajuste + " (nuevo stock: " + nuevoStock + ")");
        return true;
    }
}