package repository;

import models.Pedido;
import models.TipoEnvio;
import config.FileConfig;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return leerArchivo().stream()
                .filter(p -> p.getOrden() == orden)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Busca un pedido por ID
     */
    public Pedido findById(String idPedido) {
        return leerArchivo().stream()
                .filter(p -> p.getIdPedido().equals(idPedido))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Guarda un nuevo pedido
     */
    public boolean save(Pedido pedido) {
        List<Pedido> pedidos = leerArchivo();
        
        if (pedido.getOrden() == 0) {
            int maxOrden = pedidos.stream()
                    .mapToInt(Pedido::getOrden)
                    .max()
                    .orElse(0);
            pedido.setOrden(maxOrden + 1);
        }
        
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
        return leerArchivo().stream()
                .filter(p -> p.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }
    
    // ========================================
    // ✅ MÉTODOS OPTIMIZADOS CON ENUM
    // ========================================
    
    /**
     * Busca pedidos por tipo de envío usando enum (MÉTODO PRINCIPAL)
     * @param tipoEnvio Enum del tipo de envío
     * @return Lista de pedidos con ese tipo de envío
     */
    public List<Pedido> findByTipoEnvio(TipoEnvio tipoEnvio) {
        return leerArchivo().stream()
                .filter(p -> TipoEnvio.fromDescripcion(p.getTipoEnvio()) == tipoEnvio)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca pedidos prioritarios
     * @return Lista de pedidos con envío prioritario
     */
    public List<Pedido> findPrioritarios() {
        return findByTipoEnvio(TipoEnvio.PRIORITARIO);
    }
    
    /**
     * Busca pedidos con envío express
     * @return Lista de pedidos con envío express
     */
    public List<Pedido> findExpress() {
        return findByTipoEnvio(TipoEnvio.EXPRESS);
    }
    
    /**
     * Busca pedidos con envío normal
     * @return Lista de pedidos con envío normal
     */
    public List<Pedido> findNormales() {
        return findByTipoEnvio(TipoEnvio.NORMAL);
    }
    
    /**
     * Agrupa pedidos por tipo de envío
     * @return Mapa con tipo de envío como clave y lista de pedidos como valor
     */
    public java.util.Map<TipoEnvio, List<Pedido>> agruparPorTipoEnvio() {
        return leerArchivo().stream()
                .collect(Collectors.groupingBy(
                    p -> TipoEnvio.fromDescripcion(p.getTipoEnvio())
                ));
    }
    
    /**
     * Cuenta pedidos por tipo de envío
     * @return Mapa con tipo de envío como clave y cantidad como valor
     */
    public java.util.Map<TipoEnvio, Long> contarPorTipoEnvio() {
        return leerArchivo().stream()
                .collect(Collectors.groupingBy(
                    p -> TipoEnvio.fromDescripcion(p.getTipoEnvio()),
                    Collectors.counting()
                ));
    }
    
    /**
     * Calcula el costo total de envíos por tipo
     * @return Mapa con tipo de envío y costo total acumulado
     */
    public java.util.Map<TipoEnvio, Double> calcularCostosPorTipoEnvio() {
        return leerArchivo().stream()
                .collect(Collectors.groupingBy(
                    p -> TipoEnvio.fromDescripcion(p.getTipoEnvio()),
                    Collectors.summingDouble(Pedido::calcularCostoEnvio)
                ));
    }
    
    /**
     * Obtiene estadísticas de tipos de envío
     * @return Objeto con estadísticas completas
     */
    public EstadisticasTipoEnvio obtenerEstadisticas() {
        List<Pedido> pedidos = leerArchivo();
        
        java.util.Map<TipoEnvio, Long> conteo = contarPorTipoEnvio();
        java.util.Map<TipoEnvio, Double> costos = calcularCostosPorTipoEnvio();
        
        return new EstadisticasTipoEnvio(conteo, costos, pedidos.size());
    }
    
    /**
     * Clase interna para estadísticas
     */
    public static class EstadisticasTipoEnvio {
        private final java.util.Map<TipoEnvio, Long> cantidadPorTipo;
        private final java.util.Map<TipoEnvio, Double> costosPorTipo;
        private final int totalPedidos;
        
        public EstadisticasTipoEnvio(
                java.util.Map<TipoEnvio, Long> cantidadPorTipo,
                java.util.Map<TipoEnvio, Double> costosPorTipo,
                int totalPedidos) {
            this.cantidadPorTipo = cantidadPorTipo;
            this.costosPorTipo = costosPorTipo;
            this.totalPedidos = totalPedidos;
        }
        
        public java.util.Map<TipoEnvio, Long> getCantidadPorTipo() {
            return cantidadPorTipo;
        }
        
        public java.util.Map<TipoEnvio, Double> getCostosPorTipo() {
            return costosPorTipo;
        }
        
        public int getTotalPedidos() {
            return totalPedidos;
        }
        
        public double getPorcentaje(TipoEnvio tipo) {
            long cantidad = cantidadPorTipo.getOrDefault(tipo, 0L);
            return totalPedidos > 0 ? (cantidad * 100.0 / totalPedidos) : 0.0;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== ESTADÍSTICAS DE TIPOS DE ENVÍO ===\n");
            sb.append("Total de pedidos: ").append(totalPedidos).append("\n\n");
            
            for (TipoEnvio tipo : TipoEnvio.values()) {
                long cantidad = cantidadPorTipo.getOrDefault(tipo, 0L);
                double costo = costosPorTipo.getOrDefault(tipo, 0.0);
                double porcentaje = getPorcentaje(tipo);
                
                sb.append(String.format("%s:\n", tipo.getDescripcion()));
                sb.append(String.format("  - Cantidad: %d (%.1f%%)\n", cantidad, porcentaje));
                sb.append(String.format("  - Costo total: S/ %.2f\n", costo));
                sb.append(String.format("  - Costo promedio: S/ %.2f\n\n", 
                    cantidad > 0 ? costo / cantidad : 0.0));
            }
            
            return sb.toString();
        }
    }
}