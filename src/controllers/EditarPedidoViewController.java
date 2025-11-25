package controllers;

import models.Pedido;
import models.Cliente;
import models.Producto;
import repository.PedidoRepositorio;
import repository.ProductoRepositorio;

/**
 * EditarPedidoViewController - Controlador para editar pedidos existentes
 */
public class EditarPedidoViewController {
    
    private HomeController homeController;
    private PedidoRepositorio pedidoRepository;
    private ProductoRepositorio productoRepository;
    private DashboardViewController dashboardController;
    private Pedido pedidoActual;
    
    public void initialize() {
        homeController = HomeController.getInstance();
        pedidoRepository = new PedidoRepositorio();
        productoRepository = new ProductoRepositorio();
        System.out.println("EditarPedidoViewController inicializado");
    }
    
    /**
     * Establece el dashboard controller
     */
    public void setDashboardController(DashboardViewController controller) {
        this.dashboardController = controller;
    }
    
    /**
     * Carga un pedido para editar
     */
    public boolean cargarPedido(int nroOrden) {
        pedidoActual = pedidoRepository.findByOrden(nroOrden);
        
        if (pedidoActual == null) {
            System.err.println("Error: Pedido no encontrado");
            return false;
        }
        
        System.out.println("Pedido cargado para editar: #" + nroOrden);
        return true;
    }
    
    /**
     * Obtiene el pedido actual
     */
    public Pedido getPedidoActual() {
        return pedidoActual;
    }
    
    /**
     * Obtiene todos los productos disponibles
     */
    public java.util.List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }
    
    /**
     * Modifica los datos del cliente del pedido
     */
    public boolean modificarCliente(String dni, String nombre, String celular) {
        if (pedidoActual == null) {
            System.err.println("Error: No hay pedido cargado");
            return false;
        }
        
        // Validar campos
        if (dni == null || dni.trim().isEmpty()) {
            System.err.println("Error: El DNI no puede estar vacío");
            return false;
        }
        
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("Error: El nombre no puede estar vacío");
            return false;
        }
        
        if (celular == null || celular.trim().isEmpty()) {
            System.err.println("Error: El celular no puede estar vacío");
            return false;
        }
        
        // Actualizar datos del cliente
        Cliente cliente = pedidoActual.getCliente();
        cliente.setDni(dni.trim());
        cliente.setNombre(nombre.trim());
        cliente.setTelefono(celular.trim());
        
        System.out.println("Datos del cliente actualizados");
        return true;
    }
    
    /**
     * Modifica el producto y cantidad del pedido
     */
    public boolean modificarProducto(String sku, int nuevaCantidad) {
        if (pedidoActual == null) {
            System.err.println("Error: No hay pedido cargado");
            return false;
        }
        
        // Validar que se seleccionó un producto
        if (sku == null || sku.trim().isEmpty() || sku.equals("Seleccionar...")) {
            System.err.println("Error: Debe seleccionar un producto");
            return false;
        }
        
        // Validar cantidad
        if (nuevaCantidad <= 0) {
            System.err.println("Error: La cantidad debe ser mayor a 0");
            return false;
        }
        
        // Buscar el nuevo producto
        Producto nuevoProducto = productoRepository.findBySku(sku);
        if (nuevoProducto == null) {
            System.err.println("Error: Producto no encontrado");
            return false;
        }
        
        // Obtener datos del producto anterior
        Producto productoAnterior = pedidoActual.getProducto();
        int cantidadAnterior = pedidoActual.getCantidad();
        
        // Solo validar/ajustar stock si el pedido está CONFIRMADO
        String estado = pedidoActual.getEstado();
        if (estado.equals("CONFIRMADO") || estado.equals("CONFIRMADO SIN ADELANTO")) {
            
            // Si cambió el producto
            if (!productoAnterior.getSku().equals(sku)) {
                
                // Devolver stock del producto anterior
                productoAnterior.setStock(productoAnterior.getStock() + cantidadAnterior);
                productoRepository.update(productoAnterior);
                
                // Validar stock del nuevo producto
                if (nuevoProducto.getStock() < nuevaCantidad) {
                    System.err.println("Error: Stock insuficiente del nuevo producto");
                    System.err.println("Stock disponible: " + nuevoProducto.getStock());
                    
                    // Revertir cambio en producto anterior
                    productoAnterior.setStock(productoAnterior.getStock() - cantidadAnterior);
                    productoRepository.update(productoAnterior);
                    return false;
                }
                
                // Descontar stock del nuevo producto
                nuevoProducto.setStock(nuevoProducto.getStock() - nuevaCantidad);
                productoRepository.update(nuevoProducto);
                
                // Registrar movimientos si hay dashboardController
                if (dashboardController != null) {
                    // Ingreso del producto anterior
                    dashboardController.registrarMovimientoInventario(
                        productoAnterior.getSku(),
                        productoAnterior.getNombre(),
                        "INGRESO - EDICIÓN PEDIDO",
                        cantidadAnterior
                    );
                    
                    // Salida del nuevo producto
                    dashboardController.registrarMovimientoInventario(
                        nuevoProducto.getSku(),
                        nuevoProducto.getNombre(),
                        "SALIDA - EDICIÓN PEDIDO",
                        nuevaCantidad
                    );
                }
                
            } else {
                // Mismo producto, diferente cantidad
                int diferencia = nuevaCantidad - cantidadAnterior;
                
                if (diferencia > 0) {
                    // Aumentó la cantidad - validar stock
                    if (nuevoProducto.getStock() < diferencia) {
                        System.err.println("Error: Stock insuficiente");
                        System.err.println("Stock disponible: " + nuevoProducto.getStock());
                        return false;
                    }
                    
                    // Descontar diferencia
                    nuevoProducto.setStock(nuevoProducto.getStock() - diferencia);
                    productoRepository.update(nuevoProducto);
                    
                    // Registrar movimiento
                    if (dashboardController != null) {
                        dashboardController.registrarMovimientoInventario(
                            nuevoProducto.getSku(),
                            nuevoProducto.getNombre(),
                            "SALIDA - AUMENTO CANTIDAD",
                            diferencia
                        );
                    }
                    
                } else if (diferencia < 0) {
                    // Disminuyó la cantidad - devolver stock
                    nuevoProducto.setStock(nuevoProducto.getStock() + Math.abs(diferencia));
                    productoRepository.update(nuevoProducto);
                    
                    // Registrar movimiento
                    if (dashboardController != null) {
                        dashboardController.registrarMovimientoInventario(
                            nuevoProducto.getSku(),
                            nuevoProducto.getNombre(),
                            "INGRESO - REDUCCIÓN CANTIDAD",
                            Math.abs(diferencia)
                        );
                    }
                }
            }
        }
        
        // Actualizar el pedido
        pedidoActual.setProducto(nuevoProducto);
        pedidoActual.setCantidad(nuevaCantidad);
        
        System.out.println("Producto actualizado en el pedido");
        return true;
    }
    
    /**
     * Guarda todos los cambios del pedido
     */
    public boolean guardarCambios() {
        if (pedidoActual == null) {
            System.err.println("Error: No hay pedido cargado");
            return false;
        }
        
        // Guardar en el repositorio
        boolean actualizado = pedidoRepository.update(pedidoActual);
        
        if (actualizado) {
            System.out.println("Pedido actualizado exitosamente");
            
            // Actualizar tablas en el dashboard
            if (dashboardController != null) {
                dashboardController.actualizarTablaVentas();
                dashboardController.actualizarTablaConteoInventario();
            }
            
            return true;
        } else {
            System.err.println("Error: No se pudo actualizar el pedido");
            return false;
        }
    }
    
    /**
     * Valida si el pedido puede ser editado
     */
    public boolean puedeEditar() {
        if (pedidoActual == null) {
            return false;
        }
        
        // No se puede editar pedidos cancelados
        String estado = pedidoActual.getEstado();
        return !estado.equals("CANCELADO");
    }
}