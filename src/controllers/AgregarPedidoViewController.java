package controllers;

import models.Pedido;
import models.Cliente;
import models.Producto;
import repository.PedidoRepositorio;
import repository.ProductoRepositorio;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * AgregarPedidoViewController - Controlador para agregar pedidos
 */
public class AgregarPedidoViewController {
    
    private PedidoRepositorio pedidoRepository;
    private ProductoRepositorio productoRepository;
    private DashboardViewController dashboardController;
    
    public void initialize() {
        pedidoRepository = new PedidoRepositorio();
        productoRepository = new ProductoRepositorio();
        System.out.println("AgregarPedidoViewController inicializado");
    }
    
    /**
     * Establece el dashboard controller
     */
    public void setDashboardController(DashboardViewController controller) {
        this.dashboardController = controller;
    }
    
    /**
     * Obtiene todos los productos disponibles
     */
    public java.util.List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }
    
    /**
     * Registra un nuevo pedido
     */
    public boolean registrarPedido(String dni, String nombre, String celular, 
                                    String sku, int cantidad) {
        // Validar campos del cliente
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
        
        // Validar producto y cantidad
        if (sku == null || sku.trim().isEmpty() || sku.equals("Seleccionar...")) {
            System.err.println("Error: Debe seleccionar un producto");
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
        
        // Validar stock disponible
        if (producto.getStock() < cantidad) {
            System.err.println("Error: Stock insuficiente. Stock disponible: " + producto.getStock());
            return false;
        }
        
        // Crear el cliente
        Cliente cliente = new Cliente(dni.trim(), nombre.trim(), celular.trim());
        
        // Crear el pedido
        Pedido pedido = new Pedido(producto, cliente, cantidad);
        
        // Establecer fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        pedido.setFechaPedido(sdf.format(new Date()));
        
        // Estado inicial: PENDIENTE (ya viene por defecto en el modelo)
        
        // Guardar en el repositorio
        boolean guardado = pedidoRepository.save(pedido);
        
        if (guardado) {
            System.out.println("Pedido registrado exitosamente");
            
            // Reducir stock del producto
            producto.setStock(producto.getStock() - cantidad);
            productoRepository.update(producto);
            
            // Registrar movimiento de salida en inventario
            if (dashboardController != null) {
                dashboardController.registrarMovimientoInventario(
                    sku,
                    producto.getNombre(),
                    "SALIDA - PEDIDO",
                    cantidad
                );
            }
            
            return true;
        } else {
            System.err.println("Error: No se pudo guardar el pedido");
            return false;
        }
    }
}