package repository;

import models.Pedido;
import config.FileConfig;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PedidoRepository - Maneja el acceso a datos de Pedidos
 */
public class PedidoRepositorio {
    
    private final String filePath;
    
    public PedidoRepositorio() {
        this.filePath = FileConfig.PEDIDOS_FILE;
    }
    
    /**
     * Guarda la lista completa de pedidos en el archivo
     */
    private void guardarArchivo(List<Pedido> pedidos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            oos.writeObject(pedidos);
            System.out.println("Pedidos guardados exitosamente");
        } catch (IOException e) {
            System.err.println("Error al guardar pedidos: " + e.getMessage());
        }
    }
    
    /**
     * Lee todos los pedidos del archivo
     */
    @SuppressWarnings("unchecked")
    private List<Pedido> leerArchivo() {
        File file = new File(filePath);
        
        // Si el archivo está vacío o no existe, retornar lista vacía
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath))) {
            return (List<Pedido>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al leer pedidos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtiene todos los pedidos
     */
    public List<Pedido> findAll() {
        return leerArchivo();
    }
    
    /**
     * Busca un pedido por número de orden
     */
    public Pedido findByOrden(int orden) {
        List<Pedido> pedidos = leerArchivo();
        return pedidos.stream()
                .filter(p -> p.getOrden() == orden)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Busca un pedido por ID
     */
    public Pedido findById(String idPedido) {
        List<Pedido> pedidos = leerArchivo();
        return pedidos.stream()
                .filter(p -> p.getIdPedido().equals(idPedido))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Guarda un nuevo pedido
     */
    public boolean save(Pedido pedido) {
        List<Pedido> pedidos = leerArchivo();
        
        // Asignar número de orden automático si no tiene
        if (pedido.getOrden() == 0) {
            int maxOrden = pedidos.stream()
                    .mapToInt(Pedido::getOrden)
                    .max()
                    .orElse(0);
            pedido.setOrden(maxOrden + 1);
        }
        
        // Generar ID si no tiene
        if (pedido.getIdPedido() == null || pedido.getIdPedido().isEmpty()) {
            pedido.setIdPedido("PED-" + System.currentTimeMillis());
        }
        
        pedidos.add(pedido);
        guardarArchivo(pedidos);
        return true;
    }
    
    /**
     * Actualiza un pedido existente
     */
    public boolean update(Pedido pedidoActualizado) {
        List<Pedido> pedidos = leerArchivo();
        
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getIdPedido().equals(pedidoActualizado.getIdPedido())) {
                pedidos.set(i, pedidoActualizado);
                guardarArchivo(pedidos);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Elimina un pedido por ID
     */
    public boolean delete(String idPedido) {
        List<Pedido> pedidos = leerArchivo();
        boolean removed = pedidos.removeIf(p -> p.getIdPedido().equals(idPedido));
        
        if (removed) {
            guardarArchivo(pedidos);
        }
        return removed;
    }
    
    /**
     * Busca pedidos por estado
     */
    public List<Pedido> findByEstado(String estado) {
        List<Pedido> pedidos = leerArchivo();
        List<Pedido> resultado = new ArrayList<>();
        
        for (Pedido p : pedidos) {
            if (p.getEstado().equalsIgnoreCase(estado)) {
                resultado.add(p);
            }
        }
        return resultado;
    }
    
    /**
     * Busca pedidos prioritarios
     */
    public List<Pedido> findPrioritarios() {
        List<Pedido> pedidos = leerArchivo();
        List<Pedido> resultado = new ArrayList<>();
        
        for (Pedido p : pedidos) {
            if (p.isPrioritario()) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}