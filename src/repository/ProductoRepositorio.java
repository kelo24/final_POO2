package repository;

import models.Producto;
import config.FileConfig;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductoRepository - Maneja el acceso a datos de Productos
 */
public class ProductoRepositorio {

    private final String filePath;

    public ProductoRepositorio() {
        this.filePath = FileConfig.PRODUCTOS_FILE;
    }

    /**
     * Guarda la lista completa de productos en el archivo
     */
    private void guardarArchivo(List<Producto> productos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            oos.writeObject(productos);
            System.out.println("Productos guardados exitosamente");
        } catch (IOException e) {
            System.err.println("Error al guardar productos: " + e.getMessage());
        }
    }

    /**
     * Lee todos los productos del archivo
     */
    @SuppressWarnings("unchecked")
    private List<Producto> leerArchivo() {
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath))) {
            return (List<Producto>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al leer productos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene todos los productos
     */
    public List<Producto> findAll() {
        return leerArchivo();
    }

    /**
     * Busca un producto por SKU
     */
    public Producto findBySku(String sku) {
        List<Producto> productos = leerArchivo();
        return productos.stream()
                .filter(p -> p.getSku().equals(sku))
                .findFirst()
                .orElse(null);
    }

    /**
     * Guarda un nuevo producto
     */
    public boolean save(Producto producto) {
        List<Producto> productos = leerArchivo();

        // Verificar si ya existe un producto con ese SKU
        boolean existe = productos.stream()
                .anyMatch(p -> p.getSku().equals(producto.getSku()));

        if (existe) {
            System.err.println("Ya existe un producto con el SKU: " + producto.getSku());
            return false;
        }

        productos.add(producto);
        guardarArchivo(productos);
        return true;
    }

    /**
     * Actualiza un producto existente
     */
    public boolean update(Producto productoActualizado) {
        List<Producto> productos = leerArchivo();

        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getSku().equals(productoActualizado.getSku())) {
                productos.set(i, productoActualizado);
                guardarArchivo(productos);
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina un producto por SKU
     */
    public boolean delete(String sku) {
        List<Producto> productos = leerArchivo();
        boolean removed = productos.removeIf(p -> p.getSku().equals(sku));

        if (removed) {
            guardarArchivo(productos);
        }
        return removed;
    }
}
