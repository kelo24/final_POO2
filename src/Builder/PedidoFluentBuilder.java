/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Builder;

import models.*;



public class PedidoFluentBuilder extends PedidoBuilder {

    private Pedido pedido;

    public PedidoFluentBuilder() {
        this.pedido = new Pedido();
    }

    @Override
    public PedidoBuilder setId(String id) {
        pedido.setIdPedido(id);
        return this;
    }

    @Override
    public PedidoBuilder setFecha(String fecha) {
        pedido.setFechaPedido(fecha);
        return this;
    }

    @Override
    public PedidoBuilder setCliente(Cliente cliente) {
        pedido.setCliente(cliente);
        return this;
    }

    @Override
    public PedidoBuilder setProducto(Producto producto) {
        pedido.setProducto(producto);
        return this;
    }

    @Override
    public PedidoBuilder setCantidad(int cantidad) {
        pedido.setCantidad(cantidad);
        return this;
    }

    @Override
    public PedidoBuilder setEnvio(InfoEnvio envio) {
        pedido.setEnvio(envio);
        return this;
    }

    @Override
    public PedidoBuilder setPago(InfoPago pago) {
        pedido.setPago(pago);
        return this;
    }

    @Override
    public PedidoBuilder setPrioritario(boolean p) {
        pedido.setPrioritario(p);
        return this;
    }

    @Override
    public PedidoBuilder setEstado(String estado) {
        pedido.setEstado(estado);
        return this;
    }

    @Override
    public Pedido build() {
        return pedido;
    }



 
    public void crearNuevoPedido() {
    
        this.pedido = new Pedido();
    }

   
    public void construirProducto() {}


    public void construirCantidad() {}

   
    public void construirInfoEnvio() {}

    public void construirInfoPago() {}

  
    public void construirEstado() {}

  
    public void construirPrioridad() {}

   
    public Pedido getPedido() {
        return pedido;
    }
}

