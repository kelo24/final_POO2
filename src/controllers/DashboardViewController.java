package controllers;

import views.AgregarPedidoView;
import views.CreateProductView;
import views.DashboardView;
import javax.swing.JFrame;

/**
 * DashboardViewController - Controlador para la lógica de negocio del Dashboard
 */
public class DashboardViewController {

    private java.util.List<models.MovimientoInventario> movimientos = new java.util.ArrayList<>();

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
     * Abre la vista para crear un nuevo producto
     */
    public void abrirCreateProductView() {
        System.out.println("Abriendo vista de crear producto...");

        JFrame frame = new JFrame("Crear Producto");
        CreateProductView createProductView = new CreateProductView();

        // Pasar referencia del DashboardViewController
        createProductView.setDashboardController(this);

        // Pasar referencia del JFrame padre para poder cerrarlo
        createProductView.setParentFrame(frame);

        createProductView.setBorder(null);
        ((javax.swing.plaf.basic.BasicInternalFrameUI) createProductView.getUI()).setNorthPane(null);

        frame.setContentPane(createProductView.getContentPane());
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
     *
     * @param nroOrden número de orden a editar
     */
    public void editarPedido(int nroOrden) {
        System.out.println("Editando pedido: " + nroOrden);
        // Aquí va la lógica para editar pedido
    }

    /**
     * Cambia el estado de un pedido
     *
     * @param nroOrden número de orden
     * @param nuevoEstado nuevo estado del pedido
     */
    public boolean cambiarEstadoPedido(int nroOrden, String nuevoEstado) {
        System.out.println("Cambiando estado del pedido " + nroOrden + " a: " + nuevoEstado);
        // Aquí va la lógica para cambiar estado en la BD
        return true;
    }

    // En DashboardViewController.java
    /**
     * Actualiza la tabla de inventario con todos los movimientos
     */
    public void actualizarTablaInventario() {
        if (dashboardView == null) {
            System.err.println("DashboardView no está inicializado");
            return;
        }

        // Obtener el modelo de la tabla
        javax.swing.table.DefaultTableModel model
                = (javax.swing.table.DefaultTableModel) dashboardView.getInventarioTable().getModel();

        // Limpiar tabla
        model.setRowCount(0);

        // Agregar todos los movimientos
        for (models.MovimientoInventario mov : movimientos) {
            model.addRow(new Object[]{
                mov.getNroOrden(),
                mov.getFecha(),
                mov.getSku(),
                mov.getProducto(),
                mov.getMovimiento(),
                mov.getCantidad()
            });
        }

        System.out.println("Tabla de inventario actualizada con " + movimientos.size() + " movimientos");
    }

    /**
     * Registra un movimiento de inventario y actualiza la tabla
     */
    public void registrarMovimientoInventario(String sku, String producto, String tipoMovimiento, int cantidad) {
        models.MovimientoInventario movimiento = new models.MovimientoInventario(
                sku,
                producto,
                tipoMovimiento,
                cantidad
        );

        movimientos.add(movimiento);
        actualizarTablaInventario();
    }

}
