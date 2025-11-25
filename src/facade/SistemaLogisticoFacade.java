package facade;

import Builder.PedidoBuilder;
import models.*;
import repository.PedidoRepositorio;
import repository.ProductoRepositorio;
import java.util.ArrayList;
import java.util.List;

/**
 * SistemaLogisticoFacade - Fachada que simplifica el acceso al subsistema logístico
 * Coordina: InventarioService, CourierService, ReporteService y Repositorios
 */
public class SistemaLogisticoFacade {

    // Servicios del subsistema
    private final InventarioService inventarioService;
    private final CourierService courierService;
    private final ReporteService reporteService;
    
    // Repositorios
    private final PedidoRepositorio pedidoRepository;
    private final ProductoRepositorio productoRepository;

    /**
     * Constructor - Inicializa todos los servicios del subsistema
     */
    public SistemaLogisticoFacade() {
        this.inventarioService = new InventarioService();
        this.courierService = new CourierService();
        this.reporteService = new ReporteService();
        this.pedidoRepository = new PedidoRepositorio();
        this.productoRepository = new ProductoRepositorio();
        
        System.out.println("✅ SistemaLogisticoFacade inicializado");
    }

    // ========================================
    // OPERACIONES DE PEDIDOS (con Inventario)
    // ========================================
    
    /**
     * Registra un nuevo pedido y descuenta stock
     */
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
        // Validar stock usando InventarioService
        if (!inventarioService.validarStock(producto, cantidad)) {
            System.err.println("Error: Stock insuficiente para registrar pedido");
            return null;
        }
        
        // Crear pedido usando Builder
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

        // Descontar stock usando InventarioService
        if (inventarioService.descontarStock(producto, cantidad)) {
            // Guardar producto actualizado
            productoRepository.update(producto);
            
            // Guardar pedido
            pedidoRepository.save(pedido);
            
            System.out.println("✅ Pedido registrado con descuento de stock");
            return pedido;
        }
        
        return null;
    }

    /**
     * Confirma un pedido y descuenta stock si estaba en PENDIENTE
     */
    public boolean confirmarPedido(Pedido pedido) {
        if (pedido == null) return false;
        
        // Si el pedido estaba PENDIENTE, ahora descontar stock
        if (pedido.getEstado().equals("PENDIENTE")) {
            Producto producto = pedido.getProducto();
            int cantidad = pedido.getCantidad();
            
            // Validar y descontar stock
            if (!inventarioService.descontarStock(producto, cantidad)) {
                System.err.println("Error: No se puede confirmar, stock insuficiente");
                return false;
            }
            
            // Guardar producto actualizado
            productoRepository.update(producto);
        }
        
        // Cambiar estado a CONFIRMADO
        pedido.setEstado("CONFIRMADO");
        
        // Generar tracking si no existe
        if (pedido.getEnvio() == null || 
            !courierService.tieneTrackingCompleto(pedido.getEnvio())) {
            InfoEnvio envio = courierService.generarTracking(pedido.getEnvio());
            pedido.setEnvio(envio);
        }
        
        // Guardar pedido
        pedidoRepository.update(pedido);
        
        System.out.println("✅ Pedido confirmado con stock descontado");
        return true;
    }
    
    /**
     * Cancela un pedido y devuelve stock si estaba CONFIRMADO
     */
    public boolean cancelarPedido(Pedido pedido) {
        if (pedido == null) return false;
        
        String estadoAnterior = pedido.getEstado();
        
        // Si estaba CONFIRMADO, devolver stock
        if (estadoAnterior.equals("CONFIRMADO") || 
            estadoAnterior.equals("CONFIRMADO SIN ADELANTO")) {
            
            Producto producto = pedido.getProducto();
            int cantidad = pedido.getCantidad();
            
            // Reponer stock usando InventarioService
            inventarioService.reponerStock(producto, cantidad);
            
            // Guardar producto actualizado
            productoRepository.update(producto);
            
            System.out.println("✅ Stock devuelto al cancelar pedido");
        }
        
        // Cambiar estado a CANCELADO
        pedido.setEstado("CANCELADO");
        pedidoRepository.update(pedido);
        
        return true;
    }

    /**
     * Procesa el despacho de un pedido
     */
    public void procesarDespacho(Pedido pedido) {
        if (pedido == null) return;
        
        pedido.setEstado("DESPACHADO");
        
        // Generar tracking usando CourierService
        InfoEnvio envio = courierService.generarTracking(pedido.getEnvio());
        pedido.setEnvio(envio);
        
        pedidoRepository.update(pedido);
        
        System.out.println("✅ Pedido despachado con tracking generado");
    }

    // ========================================
    // OPERACIONES DE INVENTARIO
    // ========================================
    
    /**
     * Ajusta el stock de un producto (entrada o salida manual)
     */
    public boolean ajustarInventario(String sku, int cantidad, String tipoMovimiento) {
        Producto producto = productoRepository.findBySku(sku);
        
        if (producto == null) {
            System.err.println("Error: Producto no encontrado");
            return false;
        }
        
        boolean exitoso = false;
        
        if (tipoMovimiento.equalsIgnoreCase("INGRESO")) {
            inventarioService.reponerStock(producto, cantidad);
            exitoso = true;
        } else if (tipoMovimiento.equalsIgnoreCase("SALIDA")) {
            exitoso = inventarioService.descontarStock(producto, cantidad);
        }
        
        if (exitoso) {
            productoRepository.update(producto);
            System.out.println("✅ Inventario ajustado: " + tipoMovimiento + " - " + cantidad);
        }
        
        return exitoso;
    }
    
    /**
     * Valida disponibilidad de stock para un pedido
     */
    public boolean validarDisponibilidadStock(String sku, int cantidad) {
        Producto producto = productoRepository.findBySku(sku);
        
        if (producto == null) return false;
        
        return inventarioService.validarStock(producto, cantidad);
    }
    
    /**
     * Obtiene el stock actual de un producto
     */
    public int consultarStock(String sku) {
        Producto producto = productoRepository.findBySku(sku);
        
        if (producto == null) return 0;
        
        return inventarioService.obtenerStock(producto);
    }

    // ========================================
    // OPERACIONES DE COURIER (SHALOM)
    // ========================================
    
    /**
     * Actualiza el estado de envío de un pedido consultando SHALOM
     */
    public String actualizarEstadoEnvioShalom(Pedido pedido) {
        if (pedido == null || pedido.getEnvio() == null) {
            return "SIN INFORMACIÓN";
        }
        
        // Usar CourierService para consultar SHALOM
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
     * Consulta el estado actual de un pedido
     */
    public String consultarEstadoEnvio(Pedido pedido) {
        return courierService.consultarEstado(pedido.getEnvio());
    }

    // ========================================
    // OPERACIONES DE REPORTES
    // ========================================
    
//    public String generarReporteGeneral() {
//        List<Pedido> todosPedidos = pedidoRepository.findAll();
//        return reporteService.generarResumen(todosPedidos);
//    }
    
    /**
     * Genera reporte de stock bajo
     */
    public List<Producto> obtenerProductosStockBajo(int umbral) {
        List<Producto> productos = productoRepository.findAll();
        List<Producto> stockBajo = new ArrayList<>();
        
        for (Producto p : productos) {
            if (inventarioService.obtenerStock(p) < umbral) {
                stockBajo.add(p);
            }
        }
        
        return stockBajo;
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
    
    // ========================================
    // CLASE AUXILIAR PARA RESULTADOS
    // ========================================
    
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