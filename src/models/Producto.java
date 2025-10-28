package models;
import java.io.Serializable;

public class Producto implements Serializable {
    
    private String sku;
    private String nombre;
    private double precio;
    private int stock;

    public Producto() {}
    public Producto(String sku, String nombre, double precio, int stock) {
        this.sku = sku;
        this.nombre=nombre;
        this.precio=precio;
        this.stock=stock;
    }
    
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override public String toString() { return sku + " - " + nombre; }
}