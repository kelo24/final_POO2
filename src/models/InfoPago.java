package models;

import java.io.Serializable;


public class InfoPago implements Serializable {
    
    // Attributes
    private String metodo; // Efectivo / Tarjeta / Transferencia
    private double adelanto;
    
    // SETTER AND GETTER
    public String getMetodo(){return metodo;}
    public void setMetodo(String m){metodo = m;}
    public double getAdelanto(){return adelanto;}
    public void setAdelanto(double a){adelanto = a;}
}