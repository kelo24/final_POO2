package controllers;

import models.Pedido;
import models.Cliente;
import models.Producto;
import repository.PedidoRepositorio;
import repository.ProductoRepositorio;
import factory.EntidadFactory;
import Builder.PedidoBuilder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * AgregarPedidoViewController - Controlador para agregar pedidos
 * Usa Factory + Builder para crear pedidos
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
     * Registra un nuevo pedido usando Factory + Builder
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
        
        // PASO 1: Usar Factory para crear el cliente
        Cliente cliente = EntidadFactory.crearCliente(dni.trim(), nombre.trim(), celular.trim());
        
        // PASO 2: Usar Builder para construir el pedido paso a paso
        String idPedido = "PED-" + System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = sdf.format(new Date());
        
        Pedido pedido = new PedidoBuilder()
            .setId(idPedido)
            .setFecha(fecha)
            .setProducto(producto)
            .setCliente(cliente)
            .setCantidad(cantidad)
            .setEstado("PENDIENTE")
            .build();
        
        // Guardar en el repositorio
        boolean guardado = pedidoRepository.save(pedido);
        
        if (guardado) {
            System.out.println("Pedido registrado exitosamente usando Factory + Builder Pattern");
            return true;
        } else {
            System.err.println("Error: No se pudo guardar el pedido");
            return false;
        }
    }
}