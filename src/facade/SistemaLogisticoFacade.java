package facade;

import Builder.PedidoBuilder;
import models.*;
import repository.PedidoRepositorio;
import java.util.ArrayList;
import java.util.List;

public class SistemaLogisticoFacade {

    private final List<Pedido> pedidos;
    private final InventarioService inventarioService;
    private final CourierService courierService;
    private final ReporteService reporteService;
    private final PedidoRepositorio pedidoRepository;

    public SistemaLogisticoFacade() {
        this.pedidos = new ArrayList<>();
        this.inventarioService = new InventarioService();
        this.courierService = new CourierService();
        this.reporteService = new ReporteService();
        this.pedidoRepository = new PedidoRepositorio();
    }

    // ========================================
    // MÉTODOS DE GESTIÓN DE PEDIDOS
    // ========================================
    
    public Pedido registrarPedido(
            String id,
            String fecha,
            Cliente cliente,
            Producto producto,
            int cantidad,
            InfoEnvio envio,
            InfoPago pago,
            String tipoEnvio
    ) {
        Pedido pedido = new PedidoBuilder()
                .setId(id)
                .setFecha(fecha)
                .setCliente(cliente)
                .setProducto(producto)
                .setCantidad(cantidad)
                .setEnvio(envio)
                .setPago(pago)
                .setTipoEnvio(tipoEnvio)
                .setEstado("REGISTRADO")
                .build();

        pedidos.add(pedido);
        inventarioService.descontarStock(producto, cantidad);

        return pedido;
    }

    public void procesarDespacho(Pedido pedido) {
        pedido.setEstado("DESPACHADO");
        
        // Generar tracking usando CourierService
        InfoEnvio envio = courierService.generarTracking(pedido.getEnvio());
        pedido.setEnvio(envio);
    }

    // ========================================
    // MÉTODOS PARA ACTUALIZAR ESTADOS SHALOM
    // ========================================
    
    /**
     * Actualiza el estado de envío de un pedido consultando SHALOM
     */
    public String actualizarEstadoEnvioShalom(Pedido pedido) {
        if (pedido == null || pedido.getEnvio() == null) {
            return "SIN INFORMACIÓN";
        }
        
        // Usar CourierService para consultar SHALOM (integrado)
        String nuevoEstado = courierService.actualizarEstadoDesdeShalom(pedido.getEnvio());
        
        // Guardar cambios
        pedidoRepository.update(pedido);
        
        return nuevoEstado;
    }
    
    /**
     * Actualiza todos los pedidos confirmados consultando SHALOM
     */
    public ResultadoActualizacionShalom actualizarTodosLosEstadosShalom() {
        List<Pedido> pedidosConfirmados = pedidoRepository.findByEstado("CONFIRMADO");
        pedidosConfirmados.addAll(pedidoRepository.findByEstado("CONFIRMADO SIN ADELANTO"));
        
        int actualizados = 0;
        int sinTracking = 0;
        int errores = 0;
        
        for (Pedido pedido : pedidosConfirmados) {
            InfoEnvio envio = pedido.getEnvio();
            
            // Validar que tenga tracking completo
            if (!courierService.tieneTrackingCompleto(envio)) {
                sinTracking++;
                continue;
            }
            
            try {
                // Actualizar estado desde SHALOM
                String nuevoEstado = actualizarEstadoEnvioShalom(pedido);
                
                if (!nuevoEstado.equals("SIN INFORMACIÓN") && !nuevoEstado.equals("SIN REGISTRO")) {
                    actualizados++;
                } else {
                    errores++;
                }
                
                // Pausa para no saturar la API
                Thread.sleep(500);
                
            } catch (InterruptedException e) {
                errores++;
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                errores++;
                System.err.println("Error al actualizar pedido: " + e.getMessage());
            }
        }
        
        return new ResultadoActualizacionShalom(actualizados, sinTracking, errores);
    }
    
    /**
     * Consulta el estado actual de un pedido (sin actualizar desde SHALOM)
     */
    public String consultarEstadoEnvio(Pedido pedido) {
        return courierService.consultarEstado(pedido.getEnvio());
    }

    // ========================================
    // MÉTODOS DE CONSULTA
    // ========================================
    
    public List<Pedido> obtenerPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPedidoPorId(String id) {
        return pedidoRepository.findById(id);
    }

//    public String generarReporteGeneral() {
//        List<Pedido> todosPedidos = obtenerPedidos();
//        return reporteService.generarResumen(todosPedidos);
//    }
    
    // ========================================
    // CLASE AUXILIAR PARA RESULTADOS
    // ========================================
    
    /**
     * Clase para resultados de actualización SHALOM
     */
    public static class ResultadoActualizacionShalom {
        private final int actualizados;
        private final int sinTracking;
        private final int errores;
        
        public ResultadoActualizacionShalom(int actualizados, int sinTracking, int errores) {
            this.actualizados = actualizados;
            this.sinTracking = sinTracking;
            this.errores = errores;
        }
        
        public int getActualizados() { return actualizados; }
        public int getSinTracking() { return sinTracking; }
        public int getErrores() { return errores; }
        
        @Override
        public String toString() {
            return String.format(
                "Actualización completada:\n\n" +
                "✓ Actualizados: %d\n" +
                "⚠ Sin tracking: %d\n" +
                "✗ Errores: %d",
                actualizados, sinTracking, errores
            );
        }
    }
}