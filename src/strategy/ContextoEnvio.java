/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package strategy;

import models.Pedido;


public class ContextoEnvio {

    private EstrategiaEnvio estrategia;

    public void setEstrategia(EstrategiaEnvio estrategia) {
        this.estrategia = estrategia;
    }

    public double calcularCosto(Pedido pedido) {
        if (estrategia == null) return 0;
        return estrategia.calcularCosto(pedido);
    }

    public String getNombreEstrategia() {
        return estrategia != null ? estrategia.getNombre() : "Sin estrategia";
    }
}
