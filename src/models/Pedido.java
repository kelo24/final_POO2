package models;

import java.io.Serializable;

public class Pedido implements Serializable {
    
    // Attributes
    private String idPedido;
    private String fechaPedido;
    private int orden;
    private Producto producto;
    private Cliente cliente;
    private int cantidad;
    private double precio;
    private String estado = "PENDIENTE";
    private InfoEnvio envio;
    private InfoPago pago;
    private String tipoEnvio = "Normal"; // ✅ REEMPLAZA a prioritario
    
    // Constructor
    public Pedido(){}
    public Pedido(Producto producto, Cliente cliente, int cantidad){
        this.producto = producto;
        this.cliente = cliente;
        this.cantidad = cantidad;
        if(producto != null) this.precio = producto.getPrecio() * cantidad;
    }
    
    // SETTER AND GETTER
    public String getIdPedido(){return idPedido;}
    public void setIdPedido(String id){this.idPedido=id;}
    public String getFechaPedido(){return fechaPedido;}
    public void setFechaPedido(String f){this.fechaPedido=f;}
    public int getOrden(){return orden;}
    public void setOrden(int o){this.orden=o;}
    public Producto getProducto(){return producto;}
    public void setProducto(Producto p){this.producto = p;}
    public Cliente getCliente(){return cliente;}
    public void setCliente(Cliente c){this.cliente = c;}
    public int getCantidad(){return cantidad;}
    public void setCantidad(int c) {
        this.cantidad = c;
        if (producto != null) {
            this.precio = producto.getPrecio() * c;
        }
    }
    public double getPrecio(){return precio;}
    public void setPrecio(double p){this.precio = p;}
    public String getEstado(){return estado;}
    public void setEstado(String e){this.estado = e;}
    
    // ❌ ELIMINAR estos métodos:
    // public boolean isPrioritario(){return prioritario;}
    // public void setPrioritario(boolean p){this.prioritario = p;}
    
    public InfoEnvio getEnvio(){return envio;}
    public void setEnvio(InfoEnvio e){this.envio = e;}
    public InfoPago getPago(){return pago;}
    public void setPago(InfoPago p){this.pago = p;}
    
    // ✅ Getter y Setter para tipoEnvio
    public String getTipoEnvio(){return tipoEnvio;}
    public void setTipoEnvio(String t){this.tipoEnvio = t;}

    public void cambiarInfoEnvio(String dep,String prov,String dist,String dir){
        if(envio == null) envio = new InfoEnvio();
        envio.setDepartamento(dep);
        envio.setProvincia(prov);
        envio.setDistrito(dist);
        envio.setDireccion(dir);
    }
    
    public void cambiarEstadoPedido(String nuevo){this.estado = nuevo;}
    
    public void cambiarInfoTracking(String trans,String suc,String nTr,String cTr){
        if(envio==null) envio=new InfoEnvio();
        envio.setTransportadora(trans);
        envio.setSucursal(suc);
        envio.setnTracking(nTr);
        envio.setcTracking(cTr);
    }
    
    public void cambiarEstadoTracking(String nuevo){
        if(envio != null) envio.setEstado(nuevo);
    }
    
    // ✅ Calcular costo de envío usando Strategy
    public double calcularCostoEnvio() {
        TipoEnvio tipo = TipoEnvio.fromDescripcion(tipoEnvio);
        return tipo.calcularCosto(this);
    }
    
}