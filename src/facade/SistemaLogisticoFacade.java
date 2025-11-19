/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import java.util.ArrayList;
import java.util.List;

import Builder.PedidoBuilder;
import factory.EntidadFactory;
import models.*;

public class SistemaLogisticoFacade {

    private final InventarioService inventario;
    private final CourierService courier;
    private final ReporteService reportes;

    private final List<Pedido> pedidos;

    public SistemaLogisticoFacade() {
        inventario = new InventarioService();
        courier = new CourierService();
        reportes = new ReporteService();
        pedidos = new ArrayList<>();
    }

    // ============================================================
    // 1. REGISTRO DE PEDIDO
    // ============================================================
    public Pedido registrarPedido(
            String id,
            String fecha,
            Cliente cliente,
            Producto producto,
            int cantidad,
            InfoEnvio envio,
            InfoPago pago,
            boolean prioritario
    ) {

        if (!inventario.descontarStock(producto, cantidad)) {
            System.out.println("ERROR → No hay stock suficiente");
            return null;
        }

        Pedido pedido = new PedidoBuilder()
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

        pedidos.add(pedido);

        System.out.println("Pedido registrado → " + pedido.getIdPedido());

        return pedido;
    }

    // ============================================================
    // 2. GENERAR TRACKING CON SHALOM
    // ============================================================
    public void generarTracking(Pedido pedido) {

        InfoEnvio envioActualizado = courier.generarTracking(pedido.getEnvio());
        pedido.setEnvio(envioActualizado);
        pedido.setEstado("DESPACHADO");

        System.out.println("Tracking generado: " + envioActualizado.getnTracking());
    }

    // ============================================================
    // 3. CAMBIAR ESTADO DEL PEDIDO
    // ============================================================
    public void actualizarEstado(Pedido pedido, String nuevoEstado) {
        pedido.setEstado(nuevoEstado);
        if (pedido.getEnvio() != null) pedido.getEnvio().setEstado(nuevoEstado);
        System.out.println("Estado actualizado → " + nuevoEstado);
    }

    // ============================================================
    // 4. CONSULTAR ESTADO
    // ============================================================
    public String consultarEstado(Pedido pedido) {
        if (pedido.getEnvio() == null) return "SIN ENVÍO";
        return courier.consultarEstado(pedido.getEnvio());
    }

    // ============================================================
    // 5. REPORTE COMPLETO
    // ============================================================
    public void generarReporte() {
        reportes.generarResumen(pedidos);
    }

    // ============================================================
    // 6. OBTENER LISTA DE PEDIDOS
    // ============================================================
    public List<Pedido> getPedidos() {
        return pedidos;
    }
}
