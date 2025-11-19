/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import models.Producto;

public class InventarioService {

    public boolean descontarStock(Producto p, int cantidad) {
        if (p.getStock() < cantidad)
            return false;
        p.setStock(p.getStock() - cantidad);
        return true;
    }

    public void reponerStock(Producto p, int cantidad) {
        p.setStock(p.getStock() + cantidad);
    }
}


