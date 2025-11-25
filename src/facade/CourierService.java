package facade;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import models.InfoEnvio;

public class CourierService {

    private static final String API_BASE_URL = "https://servicesweb.shalomcontrol.com/api/v1/web/rastrea";

    /**
     * Genera información de tracking para un envío
     */
    public InfoEnvio generarTracking(InfoEnvio envio) {
        if (envio == null) {
            envio = new InfoEnvio();
        }

        envio.setTransportadora("Shalom");
        envio.setSucursal("Lima - Central");
        envio.setnTracking(UUID.randomUUID().toString().substring(0, 10));
        envio.setcTracking("SH-" + System.currentTimeMillis());
        envio.setEstadoEnvio("REGISTRADO");

        return envio;
    }

    /**
     * Consulta el estado actual de un envío (sin actualizar desde API)
     */
    public String consultarEstado(InfoEnvio envio) {
        if (envio == null) {
            return "SIN INFORMACIÓN";
        }
        return envio.getEstadoEnvio() != null ? envio.getEstadoEnvio() : "SIN REGISTRO";
    }

    /**
     * Actualiza el estado de un envío consultando la API de SHALOM
     */
    public String actualizarEstadoDesdeShalom(InfoEnvio envio) {
        if (envio == null) {
            return "SIN INFORMACIÓN";
        }

        String nroTracking = envio.getnTracking();
        String codTracking = envio.getcTracking();

        // Validar que tenga datos de tracking
        if (nroTracking == null || nroTracking.trim().isEmpty()
                || codTracking == null || codTracking.trim().isEmpty()) {
            return "SIN REGISTRO";
        }

        try {
            // Consultar API de SHALOM
            String nuevoEstado = actualizarEstadoShalom(nroTracking, codTracking);

            // Actualizar el estado en el objeto InfoEnvio
            envio.setEstadoEnvio(nuevoEstado);

            return nuevoEstado;

        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.err.println("Error al consultar SHALOM: " + e.getMessage());
            return envio.getEstadoEnvio(); // Retornar el estado anterior
        }
    }

    /**
     * Actualiza el estado consultando la API de SHALOM (Método completo integrado)
     * @param numero Número de tracking
     * @param codigo Código de tracking
     * @return Nuevo estado del envío
     */
    public String actualizarEstadoShalom(String numero, String codigo)
            throws URISyntaxException, IOException, InterruptedException {

        // Validar parámetros
        if (numero == null || numero.trim().isEmpty()
                || codigo == null || codigo.trim().isEmpty()) {
            return "SIN REGISTRO";
        }

        String oseId = "";
        String nuevoEstado = "";

        // ******************* PASO 1: BUSCAR EL OSE_ID *******************
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Cuerpo de la solicitud POST en formato JSON
            String bodyRequest = String.format("{\"numero\":\"%s\",\"codigo\":\"%s\"}", numero, codigo);

            // HTTP Request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(API_BASE_URL + "/buscar"))
                    .POST(HttpRequest.BodyPublishers.ofString(bodyRequest))
                    .header("Content-Type", "application/json")
                    .build();

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Extraer el ose_id del JSON
            String responseBody = response.body();
            int startIdx = responseBody.indexOf("\"ose_id\":") + "\"ose_id\":".length();

            if (startIdx >= "\"ose_id\":".length()) {
                int endIdx = responseBody.indexOf(",", startIdx);
                if (endIdx == -1) {
                    endIdx = responseBody.indexOf("}", startIdx);
                }

                oseId = responseBody.substring(startIdx, endIdx).trim();
                oseId = oseId.replace("\"", ""); // Limpiar comillas
            }

        } catch (InterruptedException e) {
            System.out.println("La solicitud fue interrumpida al buscar OSE_ID.");
            Thread.currentThread().interrupt();
            return "SIN REGISTRO";
        }

        // Si no se obtuvo ose_id, retornar
        if (oseId == null || oseId.isEmpty()) {
            return "SIN REGISTRO";
        }

        // ******************* PASO 2: BUSCAR EL ESTADO DEL ENVÍO *******************
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Cuerpo de la solicitud POST en formato JSON
            String bodyRequest = String.format("{\"ose_id\":\"%s\"}", oseId);

            // HTTP Request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(API_BASE_URL + "/estados"))
                    .POST(HttpRequest.BodyPublishers.ofString(bodyRequest))
                    .header("Content-Type", "application/json")
                    .build();

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Determinar el estado basándose en la respuesta
            nuevoEstado = determinarEstado(response.body());

        } catch (InterruptedException e) {
            System.out.println("La solicitud fue interrumpida al buscar estados.");
            Thread.currentThread().interrupt();
            return "SIN REGISTRO";
        }

        return nuevoEstado;
    }

    /**
     * Determina el estado del envío basándose en la respuesta de la API
     */
    private String determinarEstado(String responseBody) {
        try {
            // Buscar las posiciones de los estados
            int registradoIndex = responseBody.indexOf("\"registrado\"");
            int origenIndex = responseBody.indexOf("\"origen\"");
            int transitoIndex = responseBody.indexOf("\"transito\"");
            int destinoIndex = responseBody.indexOf("\"destino\"");
            int entregadoIndex = responseBody.indexOf("\"entregado\"");
            int repartoIndex = responseBody.indexOf("\"reparto\"");

            // Si no se encuentra alguno de los índices principales
            if (registradoIndex == -1 || origenIndex == -1) {
                return "SIN REGISTRO";
            }

            // Verificar cuál estado es el último sin null
            if (responseBody.substring(registradoIndex, origenIndex).contains("null")) {
                return "SIN REGISTRO";
            } else if (transitoIndex != -1 && responseBody.substring(origenIndex, transitoIndex).contains("null")) {
                return "REGISTRADO";
            } else if (destinoIndex != -1 && responseBody.substring(transitoIndex, destinoIndex).contains("null")) {
                return "EN ORIGEN";
            } else if (entregadoIndex != -1 && responseBody.substring(destinoIndex, entregadoIndex).contains("null")) {
                return "EN TRANSITO";
            } else if (repartoIndex != -1 && responseBody.substring(entregadoIndex, repartoIndex).contains("null")) {
                return "EN DESTINO";
            } else if (repartoIndex != -1 && !responseBody.substring(entregadoIndex, repartoIndex).contains("null")) {
                return "ENTREGADO";
            } else {
                return "EN DESTINO";
            }

        } catch (Exception e) {
            System.err.println("Error al determinar estado: " + e.getMessage());
            return "SIN REGISTRO";
        }
    }

    /**
     * Valida si un envío tiene información de tracking completa
     */
    public boolean tieneTrackingCompleto(InfoEnvio envio) {
        if (envio == null) {
            return false;
        }

        String nroTracking = envio.getnTracking();
        String codTracking = envio.getcTracking();

        return nroTracking != null && !nroTracking.trim().isEmpty()
                && codTracking != null && !codTracking.trim().isEmpty();
    }
}