/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Builder;


import models.*;

public class PedidoDirector {

    private PedidoBuilder builder;

    public PedidoDirector(PedidoBuilder builder) {
        this.builder = builder;
    }

    public Pedido construirPedidoBasico(
            String id,
            String fecha,
            Cliente cliente,
            Producto producto,
            int cantidad
    ) {
        return builder
                .setId(id)
                .setFecha(fecha)
                .setCliente(cliente)
                .setProducto(producto)
                .setCantidad(cantidad)
                .setEstado("REGISTRADO")
                .build();
    }

    public Pedido construirPedidoCompleto(
            String id,
            String fecha,
            Cliente cliente,
            Producto producto,
            int cantidad,
            InfoEnvio envio,
            InfoPago pago,
            boolean prioritario
    ) {
        return builder
                .setId(id)
                .setFecha(fecha)
                .setCliente(cliente)
                .setProducto(producto)
                .setCantidad(cantidad)
                .setEnvio(envio)
                .setPago(pago)
                .setPrioritario(prioritario)
                .setEstado("REGISTRADO")
                .build();
    }
}


