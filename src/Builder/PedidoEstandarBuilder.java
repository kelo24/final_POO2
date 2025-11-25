
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Builder;
import models.*;

        
public class PedidoEstandarBuilder extends PedidoBuilder {

    private Pedido pedido;

    public void crearNuevoPedido() {
        pedido = new Pedido();
    }

    public void construirProducto() {
        Producto p = new Producto();
        p.setSku("SKU001");
        p.setNombre("Producto Ejemplo");
        p.setPrecio(20.0);
        p.setStock(10);
        pedido.setProducto(p);
    }

    public void construirCantidad() {
        pedido.setCantidad(3);
    }

    public void construirInfoEnvio() {
        InfoEnvio envio = new InfoEnvio();
        envio.setDepartamento("Lima");
        envio.setProvincia("Lima");
        envio.setDistrito("Miraflores");
        envio.setDireccion("Av. Siempre Viva 123");
        pedido.setEnvio(envio);
    }

    public void construirInfoPago() {
        InfoPago pago = new InfoPago();
        pago.setMetodo("Tarjeta Crédito");
        pago.setAdelanto(50.0);
        pedido.setPago(pago);
    }

    
    public void construirEstado() {
        pedido.setEstado("REGISTRADO");
    }

    
    public void construirPrioridad() {
        pedido.setPrioritario(true);
    }

  
    public Pedido getPedido() {
        return pedido;
    }

    // ----------------------------------------------------
    // Estos métodos del Fluent Builder se dejan VACÍOS
    // para que no afecten al Builder Estandar
    // ----------------------------------------------------

    @Override public PedidoBuilder setId(String id) { return this; }
    @Override public PedidoBuilder setFecha(String fecha) { return this; }
    @Override public PedidoBuilder setCliente(Cliente cliente) { return this; }
    @Override public PedidoBuilder setProducto(Producto producto) { return this; }
    @Override public PedidoBuilder setCantidad(int cantidad) { return this; }
    @Override public PedidoBuilder setEnvio(InfoEnvio envio) { return this; }
    @Override public PedidoBuilder setPago(InfoPago pago) { return this; }
    @Override public PedidoBuilder setPrioritario(boolean p) { return this; }
    @Override public PedidoBuilder setEstado(String estado) { return this; }
    @Override public Pedido build() { return getPedido(); }
}
