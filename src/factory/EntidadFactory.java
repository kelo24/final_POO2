package factory;

import models.*;

/** Fábrica simple para instanciar entidades del dominio */
public class EntidadFactory {

    public static Cliente crearCliente(String dni, String nombre, String telefono){
        return new Cliente(dni, nombre, telefono);
    }

    public static Producto crearProducto(String sku, String nombre, double precio, int stock){
        return new Producto(sku, nombre, precio, stock);
    }

    public static Pedido crearPedidoBase(String id, String fecha){
        Pedido p = new Pedido();
        p.setIdPedido(id);
        p.setFechaPedido(fecha);
        return p;
    }
}
