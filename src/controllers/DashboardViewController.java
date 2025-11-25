package controllers;

import views.AgregarPedidoView;
import views.DashboardView;
import javax.swing.JFrame;

/**
 * DashboardViewController - Controlador para la lógica de negocio del Dashboard
 */
public class DashboardViewController {
    
    private HomeController homeController;
    private DashboardView dashboardView;
    
    public void initialize() {
        homeController = HomeController.getInstance();
        System.out.println("DashboardViewController inicializado");
    }
    
    /**
     * Establece la referencia al DashboardView
     */
    public void setDashboardView(DashboardView view) {
        this.dashboardView = view;
    }
    
    /**
     * Abre la vista para agregar un nuevo pedido
     */
    public void abrirAgregarPedidoView() {
        System.out.println("Abriendo vista de agregar pedido...");
        
        // Crear un JFrame temporal para contener el JInternalFrame
        JFrame frame = new JFrame("Agregar Pedido");
        AgregarPedidoView agregarPedidoView = new AgregarPedidoView();
        
        // Remover los bordes del JInternalFrame
        agregarPedidoView.setBorder(null);
        ((javax.swing.plaf.basic.BasicInternalFrameUI) agregarPedidoView.getUI()).setNorthPane(null);
        
        frame.setContentPane(agregarPedidoView.getContentPane());
        frame.pack();
        frame.setLocationRelativeTo(dashboardView);
        frame.setVisible(true);
    }
    
    /**
     * Agrega un nuevo pedido
     */
    public void agregarPedido() {
        System.out.println("Agregando nuevo pedido...");
        // Aquí va la lógica para agregar pedido
    }
    
    /**
     * Edita un pedido existente
     * @param nroOrden número de orden a editar
     */
    public void editarPedido(int nroOrden) {
        System.out.println("Editando pedido: " + nroOrden);
        // Aquí va la lógica para editar pedido
    }
    
    /**
     * Cambia el estado de un pedido
     * @param nroOrden número de orden
     * @param nuevoEstado nuevo estado del pedido
     */
    public boolean cambiarEstadoPedido(int nroOrden, String nuevoEstado) {
        System.out.println("Cambiando estado del pedido " + nroOrden + " a: " + nuevoEstado);
        // Aquí va la lógica para cambiar estado en la BD
        return true;
    }
}