// Crear archivo: models/MovimientoInventario.java
package models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovimientoInventario implements Serializable {

    private static final long serialVersionUID = 1L;
    private static int contadorOrden = 0;

    private int nroOrden;
    private String fecha;
    private String sku;
    private String producto;
    private String movimiento; // ENTRADA, SALIDA, AJUSTE
    private int cantidad;

    public MovimientoInventario(String sku, String producto, String movimiento, int cantidad) {
        this.nroOrden = ++contadorOrden;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.fecha = sdf.format(new Date());
        this.sku = sku;
        this.producto = producto;
        this.movimiento = movimiento;
        this.cantidad = cantidad;
    }

    // Getters
    public int getNroOrden() {
        return nroOrden;
    }

    public String getFecha() {
        return fecha;
    }

    public String getSku() {
        return sku;
    }

    public String getProducto() {
        return producto;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public int getCantidad() {
        return cantidad;
    }
}
