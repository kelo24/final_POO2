/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import Builder.PedidoBuilder;
import factory.EntidadFactory;
import strategy.*;
import models.*;

import java.util.ArrayList;
import java.util.List;

public class SistemaLogisticoFacade {

    
    private final List<Pedido> pedidos;
    private final InventarioService inventarioService;
    private final CourierService courierService;
    private final ReporteService reporteService;

  
    private final ContextoEnvio contextoEnvio;

   
    public SistemaLogisticoFacade() {
        this.pedidos = new ArrayList<>();
        this.inventarioService = new InventarioService();
        this.courierService = new CourierService();
        this.reporteService = new ReporteService();
        this.contextoEnvio = new ContextoEnvio();
    }

   
    public void seleccionarEstrategiaEnvio(EstrategiaEnvio estrategia) {
        contextoEnvio.setEstrategia(estrategia);
    }

    public double calcularCostoEnvio(Pedido pedido) {
        return contextoEnvio.calcularCosto(pedido);
    }

    public String estrategiaSeleccionada() {
        return contextoEnvio.getNombreEstrategia();
    }

    
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

        inventarioService.descontarStock(producto, cantidad);

        return pedido;
    }

    public void procesarDespacho(Pedido pedido) {
        pedido.setEstado("DESPACHADO");
        courierService.generarTracking(pedido);
    }

    public void actualizarEstadoTracking(Pedido pedido) {
        courierService.actualizarEstado(pedido);
    }

  
    public List<Pedido> obtenerPedidos() {
        return pedidos;
    }

    public Pedido buscarPedidoPorId(String id) {
        for (Pedido p : pedidos) {
            if (p.getIdPedido().equals(id)) return p;
        }
        return null;
    }

    public String generarReporteGeneral() {
        return reporteService.generarReporte(pedidos);
    }

  
    private static class InventarioService {
        public void descontarStock(Producto producto, int cantidad) {
            if (producto != null) {
                producto.setStock(producto.getStock() - cantidad);
            }
        }
    }

   
    private static class CourierService {

        public void generarTracking(Pedido pedido) {
            if (pedido.getEnvio() == null) pedido.setEnvio(new InfoEnvio());

            pedido.getEnvio().setTransportadora("Shalom");
            pedido.getEnvio().setnTracking("TRK-" + pedido.getIdPedido());
            pedido.getEnvio().setEstado("En Camino");
        }

        public void actualizarEstado(Pedido pedido) {
            if (pedido.getEnvio() != null) {
                pedido.getEnvio().setEstado("Entregado");
            }
        }
    }

   
    private static class ReporteService {

        public String generarReporte(List<Pedido> pedidos) {
            StringBuilder sb = new StringBuilder();
            sb.append("=== REPORTE LOGÍSTICO ===\n");
            sb.append("Total de pedidos: ").append(pedidos.size()).append("\n\n");

            for (Pedido p : pedidos) {
                sb.append("ID: ").append(p.getIdPedido()).append("\n")
                        .append("Cliente: ").append(p.getCliente().getNombre()).append("\n")
                        .append("Producto: ").append(p.getProducto().getNombre()).append("\n")
                        .append("Cantidad: ").append(p.getCantidad()).append("\n")
                        .append("Estado: ").append(p.getEstado()).append("\n")
                        .append("-------------------------\n");
            }

            return sb.toString();
        }
    }
}
