/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package factory;

import models.Producto;
import factory.Entidad;

public class ProductoCreator extends EntidadCreator {

    private String sku;
    private String nombre;
    private double precio;
    private int stock;

    public ProductoCreator(String sku, String nombre, double precio, int stock) {
        this.sku = sku;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    @Override
    public Entidad createEntidad() {
        return (Entidad) new Producto(sku, nombre, precio, stock);
    }
}

