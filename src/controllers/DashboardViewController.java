package controllers;

import views.AgregarPedidoView;
import views.CreateProductView;
import views.DashboardView;
import models.Producto;
import repository.ProductoRepositorio;
import repository.MovimientoRepository;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import repository.MovimientoInventario;

/**
 * DashboardViewController - Controlador para la lógica de negocio del Dashboard
 */
public class DashboardViewController {

    private HomeController homeController;
    private DashboardView dashboardView;
    private ProductoRepositorio productoRepository;
    private MovimientoRepository movimientoRepository;

    public void initialize() {
        homeController = HomeController.getInstance();
        productoRepository = new ProductoRepositorio();
        movimientoRepository = new MovimientoRepository();
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

        JFrame frame = new JFrame("Agregar Pedido");
        AgregarPedidoView agregarPedidoView = new AgregarPedidoView();
        
        // Pasar referencia del DashboardViewController
        agregarPedidoView.setDashboardController(this);
        
        // Pasar referencia del JFrame padre
        agregarPedidoView.setParentFrame(frame);

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
    }

    /**
     * Edita un pedido existente
     */
    public void editarPedido(int nroOrden) {
        System.out.println("Editando pedido: " + nroOrden);
    }

    /**
     * Cambia el estado de un pedido
     */
    public boolean cambiarEstadoPedido(int nroOrden, String nuevoEstado) {
        System.out.println("Cambiando estado del pedido " + nroOrden + " a: " + nuevoEstado);
        return true;
    }

    /**
     * Actualiza la tabla de inventario con todos los movimientos
     */
    public void actualizarTablaInventario() {
        if (dashboardView == null) {
            System.err.println("DashboardView no está inicializado");
            return;
        }

        // Obtener movimientos desde el repositorio
        List<MovimientoInventario> movimientos = movimientoRepository.findAll();

        // Obtener el modelo de la tabla
        DefaultTableModel model = (DefaultTableModel) dashboardView.getInventarioTable().getModel();

        // Limpiar tabla
        model.setRowCount(0);

        // Agregar todos los movimientos
        for (MovimientoInventario mov : movimientos) {
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
     * Actualiza la tabla de conteo de inventario (reportes)
     */
    public void actualizarTablaConteoInventario() {
        if (dashboardView == null) {
            System.err.println("DashboardView no está inicializado");
            return;
        }

        // Obtener productos desde el repositorio
        List<Producto> productos = productoRepository.findAll();

        // Obtener el modelo de la tabla de reportes
        DefaultTableModel model = (DefaultTableModel) dashboardView.getConteoTable().getModel();

        // Limpiar tabla
        model.setRowCount(0);

        // Agregar todos los productos con su stock actual
        for (Producto p : productos) {
            model.addRow(new Object[]{
                p.getSku(),
                p.getNombre(),
                p.getPrecio(),
                p.getStock()
            });
        }

        System.out.println("Tabla de conteo actualizada con " + productos.size() + " productos");
    }
    
    /**
     * Actualiza el combo de productos en el dashboard
     */
    public void actualizarComboProductos() {
        if (dashboardView != null) {
            dashboardView.actualizarComboProductos();
        }
    }
    
    /**
     * Actualiza la tabla de ventas (pedidos)
     */
    public void actualizarTablaVentas() {
        if (dashboardView == null) {
            System.err.println("DashboardView no está inicializado");
            return;
        }

        // Obtener pedidos desde el repositorio
        repository.PedidoRepositorio pedidoRepo = new repository.PedidoRepositorio();
        java.util.List<models.Pedido> pedidos = pedidoRepo.findAll();

        // Obtener el modelo de la tabla de ventas
        DefaultTableModel model = (DefaultTableModel) dashboardView.getVentasTable().getModel();

        // Limpiar tabla
        model.setRowCount(0);

        // Agregar todos los pedidos
        for (models.Pedido p : pedidos) {
            model.addRow(new Object[]{
                p.getOrden(),
                p.getFechaPedido(),
                p.getCliente().getDni(),
                p.getCliente().getNombre(),
                p.getProducto().getNombre(),
                p.getCantidad(),
                p.getPrecio(),
                p.getEstado()
            });
        }

        System.out.println("Tabla de ventas actualizada con " + pedidos.size() + " pedidos");
    }

    /**
     * Registra un movimiento de inventario y actualiza las tablas
     */
    public void registrarMovimientoInventario(String sku, String producto, String tipoMovimiento, int cantidad) {
        // Crear el movimiento
        MovimientoInventario movimiento = new MovimientoInventario(
            0,
            sku,
            producto,
            tipoMovimiento,
            cantidad
    );

        // Guardar en el repositorio
        movimientoRepository.save(movimiento);

        // Actualizar ambas tablas
        actualizarTablaInventario();
        actualizarTablaConteoInventario();
        
        System.out.println("Movimiento registrado y tablas actualizadas");
    }
    
    /**
     * Registra un movimiento desde el formulario del dashboard
     */
    public boolean registrarMovimientoDesdeFormulario(String sku, String tipoMovimiento, int cantidad) {
        // Validar campos
        if (sku == null || sku.trim().isEmpty() || sku.equals("Seleccionar...")) {
            System.err.println("Error: Debe seleccionar un producto");
            return false;
        }
        
        if (tipoMovimiento == null || tipoMovimiento.trim().isEmpty() || tipoMovimiento.equals("Seleccionar...")) {
            System.err.println("Error: Debe seleccionar un tipo de movimiento");
            return false;
        }
        
        if (cantidad <= 0) {
            System.err.println("Error: La cantidad debe ser mayor a 0");
            return false;
        }
        
        // Buscar el producto
        Producto producto = productoRepository.findBySku(sku);
        if (producto == null) {
            System.err.println("Error: Producto no encontrado");
            return false;
        }
        
        // Validar stock disponible para salidas
        if (tipoMovimiento.equalsIgnoreCase("Salida")) {
            if (producto.getStock() < cantidad) {
                System.err.println("Error: Stock insuficiente. Stock actual: " + producto.getStock());
                return false;
            }
        }
        
        // Actualizar stock del producto
        if (tipoMovimiento.equalsIgnoreCase("Ingreso")) {
            producto.setStock(producto.getStock() + cantidad);
        } else if (tipoMovimiento.equalsIgnoreCase("Salida")) {
            producto.setStock(producto.getStock() - cantidad);
        }
        
        // Guardar producto actualizado
        productoRepository.update(producto);
        
        // Registrar el movimiento
        registrarMovimientoInventario(sku, producto.getNombre(), tipoMovimiento.toUpperCase(), cantidad);
        
        System.out.println("Movimiento registrado exitosamente");
        return true;
    }
}