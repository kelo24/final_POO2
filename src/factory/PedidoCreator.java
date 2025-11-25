/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package factory;

import models.*;
import factory.Entidad;

public class PedidoCreator extends EntidadCreator {

    private String id;
    private String fecha;
    private Producto producto;
    private Cliente cliente;
    private int cantidad;

    public PedidoCreator(String id, String fecha, Producto producto,
                         Cliente cliente, int cantidad) {
        this.id = id;
        this.fecha = fecha;
        this.producto = producto;
        this.cliente = cliente;
        this.cantidad = cantidad;
    }

    @Override
    public Entidad createEntidad() {
        Pedido p = new Pedido();
        p.setIdPedido(id);
        p.setFechaPedido(fecha);
        p.setProducto(producto);
        p.setCliente(cliente);
        p.setCantidad(cantidad);
        return (Entidad) p;
    }
}

