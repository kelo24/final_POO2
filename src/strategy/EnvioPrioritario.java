/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package strategy;

import models.Pedido;

public class EnvioPrioritario implements EstrategiaEnvio {

    @Override
    public double calcularCosto(Pedido pedido) {
        return 20.0 + (pedido.isPrioritario() ? 5.0 : 0);
    }

    @Override
    public String getNombre() {
        return "Envío Prioritario";
    }
}

