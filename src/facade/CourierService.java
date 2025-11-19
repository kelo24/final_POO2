/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import java.util.UUID;  // Identificador Único Universal (\(128\) bits)
import models.InfoEnvio;

public class CourierService {

    public InfoEnvio generarTracking(InfoEnvio envio) {

        if (envio == null) envio = new InfoEnvio();

        envio.setTransportadora("Shalom");
        envio.setSucursal("Lima - Central");
        envio.setnTracking(UUID.randomUUID().toString().substring(0, 10));
        envio.setcTracking("SH-" + System.currentTimeMillis());
        envio.setEstado("EN CAMINO");

        return envio;
    }

    public String consultarEstado(InfoEnvio envio) {
        if (envio == null) return "SIN INFORMACIÓN";
        return envio.getEstado();
    }
}
