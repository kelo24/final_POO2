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
     * Abre la vista para editar un pedido existente
     */
    public void abrirEditarPedidoView(int nroOrden) {
        System.out.println("Abriendo vista de editar pedido #" + nroOrden);

        JFrame frame = new JFrame("Editar Pedido #" + nroOrden);
        views.EditarPedidoView editarPedidoView = new views.EditarPedidoView();

        // Pasar referencia del DashboardViewController
        editarPedidoView.setDashboardController(this);

        // Pasar referencia del JFrame padre para poder cerrarlo
        editarPedidoView.setParentFrame(frame);

        // Cargar el pedido
        editarPedidoView.cargarPedido(nroOrden);

        editarPedidoView.setBorder(null);
        ((javax.swing.plaf.basic.BasicInternalFrameUI) editarPedidoView.getUI()).setNorthPane(null);

        frame.setContentPane(editarPedidoView.getContentPane());
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

        // Buscar el pedido
        repository.PedidoRepositorio pedidoRepo = new repository.PedidoRepositorio();
        models.Pedido pedido = pedidoRepo.findByOrden(nroOrden);

        if (pedido == null) {
            System.err.println("Error: Pedido no encontrado");
            return false;
        }

        String estadoAnterior = pedido.getEstado();

        // 1. Si cambia a CONFIRMADO o CONFIRMADO SIN ADELANTO → Descontar stock
        if (estadoAnterior.equals("PENDIENTE")
                && (nuevoEstado.equals("CONFIRMADO") || nuevoEstado.equals("CONFIRMADO SIN ADELANTO"))) {

            models.Producto producto = pedido.getProducto();
            int cantidad = pedido.getCantidad();

            // Validar stock disponible
            if (producto.getStock() < cantidad) {
                System.err.println("Error: Stock insuficiente para confirmar el pedido");
                System.err.println("Stock actual: " + producto.getStock() + ", Cantidad requerida: " + cantidad);
                return false;
            }

            // Descontar stock
            producto.setStock(producto.getStock() - cantidad);
            productoRepository.update(producto);

            // Registrar movimiento de salida
            String tipoMovimiento = nuevoEstado.equals("CONFIRMADO")
                    ? "SALIDA - PEDIDO CONFIRMADO"
                    : "SALIDA - CONFIRMADO SIN ADELANTO";

            registrarMovimientoInventario(
                    producto.getSku(),
                    producto.getNombre(),
                    tipoMovimiento,
                    cantidad
            );

            // ✅ Inicializar InfoEnvio con estado "SIN REGISTRO"
        if (pedido.getEnvio() == null) {
            pedido.setEnvio(new models.InfoEnvio());
        }
        
        System.out.println("✅ Stock descontado y InfoEnvio inicializado");
        }

        // 2. Si cambia a CANCELADO desde CONFIRMADO → Devolver stock
        if ((estadoAnterior.equals("CONFIRMADO") || estadoAnterior.equals("CONFIRMADO SIN ADELANTO"))
                && nuevoEstado.equals("CANCELADO")) {

            models.Producto producto = pedido.getProducto();
            int cantidad = pedido.getCantidad();

            // Devolver stock
            producto.setStock(producto.getStock() + cantidad);
            productoRepository.update(producto);

            // Registrar movimiento de ingreso (devolución)
            registrarMovimientoInventario(
                    producto.getSku(),
                    producto.getNombre(),
                    "INGRESO - PEDIDO CANCELADO",
                    cantidad
            );

            System.out.println("✅ Stock devuelto: " + cantidad + " unidades de " + producto.getNombre());
        }

        // 3. Si cambia a CANCELADO desde PENDIENTE → No hacer nada con el stock
        if (estadoAnterior.equals("PENDIENTE") && nuevoEstado.equals("CANCELADO")) {
            System.out.println("Pedido cancelado desde PENDIENTE - Sin cambios en stock");
        }

        // Cambiar el estado del pedido
        pedido.setEstado(nuevoEstado);

        // Guardar cambios
        boolean actualizado = pedidoRepo.update(pedido);

        if (actualizado) {
            // Actualizar las tablas en la vista
            actualizarTablaVentas();
            actualizarTablaConteoInventario();
            actualizarTablaLogistica();
            
            System.out.println("✅ Estado actualizado exitosamente a: " + nuevoEstado);
        }

        return actualizado;
    }
    
    /**
 * Cambia el estado de envío de un pedido en logística
 */
public boolean cambiarEstadoEnvio(int nroOrden, String nuevoEstadoEnvio) {
    System.out.println("Cambiando estado de envío del pedido " + nroOrden + " a: " + nuevoEstadoEnvio);
    
    repository.PedidoRepositorio pedidoRepo = new repository.PedidoRepositorio();
    models.Pedido pedido = pedidoRepo.findByOrden(nroOrden);
    
    if (pedido == null) {
        System.err.println("Error: Pedido no encontrado");
        return false;
    }
    
    // Verificar que el pedido esté confirmado
    if (!pedido.getEstado().equals("CONFIRMADO") && 
        !pedido.getEstado().equals("CONFIRMADO SIN ADELANTO")) {
        System.err.println("Error: Solo se puede cambiar el estado de envío de pedidos confirmados");
        return false;
    }
    
    // Inicializar InfoEnvio si no existe
    if (pedido.getEnvio() == null) {
        pedido.setEnvio(new models.InfoEnvio());
    }
    
    // Cambiar el estado de envío
    pedido.getEnvio().setEstadoEnvio(nuevoEstadoEnvio);
    
    // Guardar cambios
    boolean actualizado = pedidoRepo.update(pedido);
    
    if (actualizado) {
        actualizarTablaLogistica();
        System.out.println("✅ Estado de envío actualizado a: " + nuevoEstadoEnvio);
    }
    
    return actualizado;
}
    
    /**
 * Actualiza la tabla de logística con pedidos confirmados
 */
public void actualizarTablaLogistica() {
    if (dashboardView == null) {
        System.err.println("DashboardView no está inicializado");
        return;
    }

    // Obtener pedidos desde el repositorio
    repository.PedidoRepositorio pedidoRepo = new repository.PedidoRepositorio();
    java.util.List<models.Pedido> todosPedidos = pedidoRepo.findAll();
    
    // Filtrar solo pedidos CONFIRMADOS o CONFIRMADO SIN ADELANTO
    java.util.List<models.Pedido> pedidosConfirmados = new java.util.ArrayList<>();
    for (models.Pedido p : todosPedidos) {
        if (p.getEstado().equals("CONFIRMADO") || 
            p.getEstado().equals("CONFIRMADO SIN ADELANTO")) {
            pedidosConfirmados.add(p);
        }
    }

    // Obtener el modelo de la tabla de logística
    javax.swing.table.DefaultTableModel model = 
        (javax.swing.table.DefaultTableModel) dashboardView.getLogisticaTable().getModel();

    // Limpiar tabla
    model.setRowCount(0);

    // Agregar todos los pedidos confirmados
    for (models.Pedido p : pedidosConfirmados) {
        // ✅ Inicializar InfoEnvio si es null
        if (p.getEnvio() == null) {
            p.setEnvio(new models.InfoEnvio());
        }
        
        models.InfoEnvio envio = p.getEnvio();
        
        // Obtener datos de envío
        String departamento = envio.getDepartamento() != null ? envio.getDepartamento() : "";
        String provincia = envio.getProvincia() != null ? envio.getProvincia() : "";
        String distrito = envio.getDistrito() != null ? envio.getDistrito() : "";
        String direccion = envio.getDireccion() != null ? envio.getDireccion() : "";
        String transportadora = envio.getTransportadora() != null ? envio.getTransportadora() : "";
        String nroTracking = envio.getnTracking() != null ? envio.getnTracking() : "";
        String codTracking = envio.getcTracking() != null ? envio.getcTracking() : "";
        
        // ✅ Obtener ESTADO DE ENVÍO (no estado del pedido)
        String estadoEnvio = envio.getEstadoEnvio() != null ? envio.getEstadoEnvio() : "SIN REGISTRO";
        
        // Determinar si es prioritario
        String prioritario = p.isPrioritario() ? "SÍ" : "NO";
        
        model.addRow(new Object[]{
            p.getOrden(),              // Nro Orden
            estadoEnvio,               // ✅ ESTADO DE ENVÍO (no estado del pedido)
            p.getCliente().getDni(),   // DNI
            p.getCliente().getNombre(), // Nombre
            p.getProducto().getNombre(), // Producto
            p.getCantidad(),           // Cantidad
            prioritario,               // Prioritario
            departamento,              // Departamento
            provincia,                 // Provincia
            distrito,                  // Distrito
            direccion,                 // Dirección
            transportadora,            // Transportadora
            nroTracking,               // Nro Tracking
            codTracking                // Cod Tracking
        });
    }

    System.out.println("Tabla de logística actualizada con " + pedidosConfirmados.size() + " pedidos confirmados");
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
