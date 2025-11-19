/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package strategy;

import models.Pedido;

public class EnvioNormal implements EstrategiaEnvio {

    @Override
    public double calcularCosto(Pedido pedido) {
        return 10.0;
    }

    @Override
    public String getNombre() {
        return "Envío Normal";
    }
}


