package factory;
import models.*;

/** Factory específica para pedidos */
public class PedidoFactory {
    public static Pedido crearPedido(String id, String fecha){
        Pedido p = new Pedido();
        p.setIdPedido(id);
        p.setFechaPedido(fecha);
        return p;
    }
    public static Pedido crearPedidoProductoCliente(String id, String fecha, Producto producto, Cliente cliente, int cantidad){
        Pedido p = crearPedido(id, fecha);
        p.setProducto(producto);
        p.setCliente(cliente);
        p.setCantidad(cantidad);
        return p;
    }
}