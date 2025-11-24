package observer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RegistroEnArchivoObserver<T> implements ObservadorRepositorio<T> {

    private final String rutaArchivo;

    public RegistroEnArchivoObserver(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public void notificar(EventoRepositorio<T> evento) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            String linea = String.format(
                "[%s] Evento: %s | Entidad: %s%n",
                evento.getFecha(),
                evento.getTipo(),
                String.valueOf(evento.getEntidad())
            );
            writer.write(linea);
        } catch (IOException e) {
            // En un sistema real lo mandarías a logs
            e.printStackTrace();
        }
    }
}
