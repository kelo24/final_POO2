/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import java.util.List;
import models.Pedido;

public class ReporteService {

    public void generarResumen(List<Pedido> pedidos) {
        System.out.println("==== REPORTE LOGÍSTICO ====");
        System.out.println("Total pedidos: " + pedidos.size());
        long entregados = pedidos.stream().filter(p -> p.getEstado().equals("ENTREGADO")).count();
        long pendientes = pedidos.stream().filter(p -> p.getEstado().equals("PENDIENTE")).count();
        long camino = pedidos.stream().filter(p -> p.getEstado().equals("EN CAMINO")).count();
        
        System.out.println("Entregados: " + entregados);
        System.out.println("Pendientes: " + pendientes);
        System.out.println("En camino: " + camino);
        System.out.println("===========================");
    }
}
