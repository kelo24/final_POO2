package repository;

import config.FileConfig;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MovimientoRepository - Maneja el acceso a datos de Movimientos de inventario
 */
public class MovimientoRepository {

    private final String filePath;

    public MovimientoRepository() {
        this.filePath = FileConfig.INVENTARIO_FILE;
    }

    /**
     * Guarda la lista completa de movimientos en el archivo
     */
    private void guardarArchivo(List<MovimientoInventario> movimientos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            oos.writeObject(movimientos);
            System.out.println("Movimientos guardados exitosamente en: " + filePath);
        } catch (IOException e) {
            System.err.println("Error al guardar movimientos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lee todos los movimientos del archivo
     */
    @SuppressWarnings("unchecked")
    private List<MovimientoInventario> leerArchivo() {
        File file = new File(filePath);

        // Si el archivo está vacío o no existe, retornar lista vacía
        if (!file.exists() || file.length() == 0) {
            System.out.println("Archivo de movimientos vacío o no existe");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath))) {
            List<MovimientoInventario> movimientos = (List<MovimientoInventario>) ois.readObject();
            System.out.println("Movimientos leídos: " + movimientos.size());
            return movimientos;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al leer movimientos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * ✅ NUEVO: Obtiene el siguiente número de orden
     */
    private int obtenerSiguienteNroOrden() {
        List<MovimientoInventario> movimientos = leerArchivo();

        if (movimientos.isEmpty()) {
            return 1;
        }

        // Buscar el número de orden máximo
        int maxOrden = movimientos.stream()
                .mapToInt(MovimientoInventario::getNroOrden)
                .max()
                .orElse(0);

        return maxOrden + 1;
    }

    /**
     * Obtiene todos los movimientos
     */
    public List<MovimientoInventario> findAll() {
        return leerArchivo();
    }

    /**
     * Guarda un nuevo movimiento
     */
    public boolean save(MovimientoInventario movimiento) {
        try {
            List<MovimientoInventario> movimientos = leerArchivo();

            // ✅ Si el movimiento tiene nroOrden = 0, asignar uno nuevo
            if (movimiento.getNroOrden() == 0) {
                int nuevoNroOrden = obtenerSiguienteNroOrden();
                // Crear nuevo movimiento con el nroOrden correcto
                movimiento = new MovimientoInventario(
                        nuevoNroOrden,
                        movimiento.getSku(),
                        movimiento.getProducto(),
                        movimiento.getMovimiento(),
                        movimiento.getCantidad()
                );
            }

            movimientos.add(movimiento);
            guardarArchivo(movimientos);
            System.out.println("Movimiento guardado con Nro Orden: " + movimiento.getNroOrden()
                    + " - " + movimiento.getMovimiento() + " - " + movimiento.getProducto());
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar movimiento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene movimientos por SKU
     */
    public List<MovimientoInventario> findBySku(String sku) {
        List<MovimientoInventario> movimientos = leerArchivo();
        List<MovimientoInventario> resultado = new ArrayList<>();

        for (MovimientoInventario m : movimientos) {
            if (m.getSku().equals(sku)) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    /**
     * Elimina todos los movimientos (para testing)
     */
    public void deleteAll() {
        guardarArchivo(new ArrayList<>());
        System.out.println("Todos los movimientos eliminados");
    }
}
