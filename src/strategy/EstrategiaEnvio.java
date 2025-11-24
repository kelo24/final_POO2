/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package strategy;

import models.Pedido;


public interface EstrategiaEnvio {
    double calcularCosto(Pedido pedido);
    String getNombre();
}