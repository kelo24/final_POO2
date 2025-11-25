package controllers;

import models.Producto;
import repository.ProductoRepositorio;
import repository.ProductoRepositorio;

/**
 * CreateProductViewController - Controlador para crear productos
 */
public class CreateProductViewController {

    private controllers.DashboardViewController dashboardController;
    private ProductoRepositorio productoRepository;

    public void initialize() {
        productoRepository = new ProductoRepositorio();
        System.out.println("CreateProductViewController inicializado");
    }

    public void setDashboardController(controllers.DashboardViewController controller) {
        this.dashboardController = controller;
    }

    /**
     * Registra un nuevo producto
     *
     * @param sku SKU del producto
     * @param nombre Nombre del producto
     * @param precio Precio del producto
     * @param cantidad Cantidad inicial en inventario
     * @return true si se guardó exitosamente
     */
    public boolean registrarProducto(String sku, String nombre, String precio, int cantidad) {
        // Validar que los campos no estén vacíos
        if (sku == null || sku.trim().isEmpty()) {
            System.err.println("Error: El SKU no puede estar vacío");
            return false;
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("Error: El nombre no puede estar vacío");
            return false;
        }

        if (precio == null || precio.trim().isEmpty()) {
            System.err.println("Error: El precio no puede estar vacío");
            return false;
        }

        // Validar y convertir el precio
        double precioDouble;
        try {
            precioDouble = Double.parseDouble(precio);
            if (precioDouble < 0) {
                System.err.println("Error: El precio no puede ser negativo");
                return false;
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: El precio debe ser un número válido");
            return false;
        }

        // Validar cantidad
        if (cantidad < 0) {
            System.err.println("Error: La cantidad no puede ser negativa");
            return false;
        }

        // Crear el producto con el constructor completo
        Producto producto = new Producto(sku.trim(), nombre.trim(), precioDouble, cantidad);

        // Guardar en el repositorio
        boolean guardado = productoRepository.save(producto);

        if (guardado) {
            System.out.println("Producto registrado exitosamente: " + sku);

            // AGREGAR ESTAS LÍNEAS - Registrar movimiento de entrada inicial
            if (dashboardController != null) {
                dashboardController.registrarMovimientoInventario(
                        sku.trim(),
                        nombre.trim(),
                        "ENTRADA INICIAL",
                        cantidad
                );
            }

        } else {
            System.err.println("Error: No se pudo guardar el producto");
        }

        return guardado;
    }
}
