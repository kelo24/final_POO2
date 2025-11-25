package controllers;

import models.Pedido;
import models.InfoPago;
import repository.PedidoRepositorio;

/**
 * PaymentInfoViewController - Controlador para información de pago
 */
public class PaymentInfoViewController {
    
    private HomeController homeController;
    private PedidoRepositorio pedidoRepository;
    private DashboardViewController dashboardController;
    private Pedido pedidoActual;
    
    public void initialize() {
        homeController = HomeController.getInstance();
        pedidoRepository = new PedidoRepositorio();
        System.out.println("PaymentInfoViewController inicializado");
    }
    
    /**
     * Establece el dashboard controller
     */
    public void setDashboardController(DashboardViewController controller) {
        this.dashboardController = controller;
    }
    
    /**
     * Carga un pedido para ver/editar su información de pago
     */
    public boolean cargarPedido(int nroOrden) {
        pedidoActual = pedidoRepository.findByOrden(nroOrden);
        
        if (pedidoActual == null) {
            System.err.println("Error: Pedido no encontrado");
            return false;
        }
        
        // Inicializar InfoPago si no existe
        if (pedidoActual.getPago() == null) {
            pedidoActual.setPago(new InfoPago());
        }
        
        System.out.println("Pedido cargado para información de pago: #" + nroOrden);
        return true;
    }
    
    /**
     * Obtiene el pedido actual
     */
    public Pedido getPedidoActual() {
        return pedidoActual;
    }
    
    /**
     * Calcula el saldo restante
     */
    public double calcularSaldoRestante() {
        if (pedidoActual == null) return 0.0;
        
        double precioTotal = pedidoActual.getPrecio();
        double costoEnvio = pedidoActual.calcularCostoEnvio(); // ✅ Usa Strategy
        double montoTotal = precioTotal + costoEnvio;
        
        InfoPago pago = pedidoActual.getPago();
        double adelanto = (pago != null) ? pago.getAdelanto() : 0.0;
        
        return montoTotal - adelanto;
    }
    
    /**
     * Actualiza el adelanto de pago
     */
    public boolean actualizarAdelanto(double adelanto) {
        if (pedidoActual == null) {
            System.err.println("Error: No hay pedido cargado");
            return false;
        }
        
        // Validar adelanto
        if (adelanto < 0) {
            System.err.println("Error: El adelanto no puede ser negativo");
            return false;
        }
        
        double precioTotal = pedidoActual.getPrecio();
        double costoEnvio = pedidoActual.calcularCostoEnvio();
        double montoTotal = precioTotal + costoEnvio;
        
        if (adelanto > montoTotal) {
            System.err.println("Error: El adelanto no puede ser mayor al monto total");
            return false;
        }
        
        // Actualizar información de pago
        InfoPago pago = pedidoActual.getPago();
        pago.setAdelanto(adelanto);
        
        System.out.println("Adelanto actualizado a: S/ " + adelanto);
        return true;
    }
    
    /**
     * Registra pago completo
     */
    public boolean registrarPagoCompleto() {
        if (pedidoActual == null) {
            System.err.println("Error: No hay pedido cargado");
            return false;
        }
        
        double precioTotal = pedidoActual.getPrecio();
        double costoEnvio = pedidoActual.calcularCostoEnvio();
        double montoTotal = precioTotal + costoEnvio;
        
        // Establecer adelanto igual al monto total
        InfoPago pago = pedidoActual.getPago();
        pago.setAdelanto(montoTotal);
        
        System.out.println("Pago completo registrado: S/ " + montoTotal);
        return true;
    }
    
    /**
     * Guarda los cambios
     */
    public boolean guardarCambios() {
        if (pedidoActual == null) {
            System.err.println("Error: No hay pedido cargado");
            return false;
        }
        
        // Guardar en el repositorio
        boolean actualizado = pedidoRepository.update(pedidoActual);
        
        if (actualizado) {
            System.out.println("Información de pago guardada exitosamente");
            
            // Actualizar tabla de logística en el dashboard
            if (dashboardController != null) {
                dashboardController.actualizarTablaLogistica();
            }
            
            return true;
        } else {
            System.err.println("Error: No se pudo guardar la información de pago");
            return false;
        }
    }
    
    /**
     * Valida si el pedido puede editar su información de pago
     */
    public boolean puedeEditarPago() {
        if (pedidoActual == null) {
            return false;
        }
        
        // Solo pedidos confirmados pueden editar info de pago
        String estado = pedidoActual.getEstado();
        return estado.equals("CONFIRMADO") || estado.equals("CONFIRMADO SIN ADELANTO");
    }
}