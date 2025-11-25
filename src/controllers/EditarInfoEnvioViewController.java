package controllers;

import models.Pedido;
import models.InfoEnvio;
import repository.PedidoRepositorio;

/**
 * EditarInfoEnvioViewController - Controlador para editar información de envío
 */
public class EditarInfoEnvioViewController {

    private HomeController homeController;
    private PedidoRepositorio pedidoRepository;
    private DashboardViewController dashboardController;
    private Pedido pedidoActual;

    public void initialize() {
        homeController = HomeController.getInstance();
        pedidoRepository = new PedidoRepositorio();
        System.out.println("EditarInfoEnvioViewController inicializado");
    }

    /**
     * Establece el dashboard controller
     */
    public void setDashboardController(DashboardViewController controller) {
        this.dashboardController = controller;
    }

    /**
     * Carga un pedido para editar su información de envío
     */
    public boolean cargarPedido(int nroOrden) {
        pedidoActual = pedidoRepository.findByOrden(nroOrden);

        if (pedidoActual == null) {
            System.err.println("Error: Pedido no encontrado");
            return false;
        }

        // Inicializar InfoEnvio si no existe
        if (pedidoActual.getEnvio() == null) {
            pedidoActual.setEnvio(new InfoEnvio());
        }

        System.out.println("Pedido cargado para editar información de envío: #" + nroOrden);
        return true;
    }

    /**
     * Obtiene el pedido actual
     */
    public Pedido getPedidoActual() {
        return pedidoActual;
    }

    /**
     * Actualiza la información de envío (dirección)
     */
    public boolean actualizarInfoEnvio(String departamento, String provincia,
            String distrito, String direccion) {
        if (pedidoActual == null) {
            System.err.println("Error: No hay pedido cargado");
            return false;
        }

        // Validar campos
        if (departamento == null || departamento.trim().isEmpty()) {
            System.err.println("Error: El departamento no puede estar vacío");
            return false;
        }

        if (provincia == null || provincia.trim().isEmpty()) {
            System.err.println("Error: La provincia no puede estar vacía");
            return false;
        }

        if (distrito == null || distrito.trim().isEmpty()) {
            System.err.println("Error: El distrito no puede estar vacío");
            return false;
        }

        if (direccion == null || direccion.trim().isEmpty()) {
            System.err.println("Error: La dirección no puede estar vacía");
            return false;
        }

        // Actualizar información de envío
        InfoEnvio envio = pedidoActual.getEnvio();
        envio.setDepartamento(departamento.trim());
        envio.setProvincia(provincia.trim());
        envio.setDistrito(distrito.trim());
        envio.setDireccion(direccion.trim());

        System.out.println("Información de envío actualizada");
        return true;
    }

    /**
     * Actualiza la información de tracking
     */
    public boolean actualizarInfoTracking(String transportadora, String sucursal,
            String nroTracking, String codTracking) {
        if (pedidoActual == null) {
            System.err.println("Error: No hay pedido cargado");
            return false;
        }

        // Validar campos (al menos uno debe estar lleno)
        if ((transportadora == null || transportadora.trim().isEmpty())
                && (sucursal == null || sucursal.trim().isEmpty())
                && (nroTracking == null || nroTracking.trim().isEmpty())
                && (codTracking == null || codTracking.trim().isEmpty())) {
            System.err.println("Error: Debe completar al menos un campo de tracking");
            return false;
        }

        // Actualizar información de tracking
        InfoEnvio envio = pedidoActual.getEnvio();
        envio.setTransportadora(transportadora != null ? transportadora.trim() : "");
        envio.setSucursal(sucursal != null ? sucursal.trim() : "");
        envio.setnTracking(nroTracking != null ? nroTracking.trim() : "");
        envio.setcTracking(codTracking != null ? codTracking.trim() : "");

        System.out.println("Información de tracking actualizada");
        return true;
    }

    /**
     * Guarda todos los cambios
     */
    public boolean guardarCambios() {
        if (pedidoActual == null) {
            System.err.println("Error: No hay pedido cargado");
            return false;
        }

        // Guardar en el repositorio
        boolean actualizado = pedidoRepository.update(pedidoActual);

        if (actualizado) {
            System.out.println("Información de envío guardada exitosamente");

            // Actualizar tabla de logística en el dashboard
            if (dashboardController != null) {
                dashboardController.actualizarTablaLogistica();
            }

            return true;
        } else {
            System.err.println("Error: No se pudo guardar la información de envío");
            return false;
        }
    }

    /**
     * Valida si el pedido puede editar su información de envío
     */
    public boolean puedeEditarEnvio() {
        if (pedidoActual == null) {
            return false;
        }

        // Solo pedidos confirmados pueden editar info de envío
        String estado = pedidoActual.getEstado();
        return estado.equals("CONFIRMADO") || estado.equals("CONFIRMADO SIN ADELANTO");
    }
}
