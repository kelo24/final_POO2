package Builder;

import models.*;

public class PedidoBuilder {
    private final Pedido p;

    public PedidoBuilder() {
        this.p = new Pedido();
    }

    public PedidoBuilder setId(String id){
        p.setIdPedido(id);
        return this;
    }

    public PedidoBuilder setFecha(String fecha){
        p.setFechaPedido(fecha);
       return this; }
       public PedidoBuilder setCliente(Cliente c){ 
           p.setCliente(c);
    return this; }
     public PedidoBuilder setProducto(Producto pr){
         p.setProducto(pr); 
       return this; }
    public PedidoBuilder setCantidad(int c){
        p.setCantidad(c);
        return this;
    }

    public PedidoBuilder setEstado(String e){
        p.setEstado(e);
        return this;
    }
    
    public PedidoBuilder setTipoEnvio(String tipo){
        p.setTipoEnvio(tipo);
        return this;
    }

    public PedidoBuilder setEnvio(InfoEnvio e){
        p.setEnvio(e);
        return this;
    }

    public PedidoBuilder setPago(InfoPago pg){
        p.setPago(pg);
        return this;
    }


    public Pedido build() {
        return this.p;
    }
}
