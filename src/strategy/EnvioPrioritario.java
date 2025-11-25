package strategy;

import models.Pedido;

public class EnvioPrioritario implements EstrategiaEnvio {

    @Override
    public double calcularCosto(Pedido pedido) {
        
        double costoBase = 20.0;
        boolean esPrioritario = pedido.getTipoEnvio() != null && 
                               pedido.getTipoEnvio().equals("Prioritario");
        return costoBase + (esPrioritario ? 5.0 : 0);
    }

    @Override
    public String getNombre() {
        return "Envío Prioritario";
    }
}