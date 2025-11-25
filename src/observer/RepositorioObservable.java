package observer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RepositorioObservable<T> {

    private final List<ObservadorRepositorio<T>> observadores = new ArrayList<>();

    public void agregarObservador(ObservadorRepositorio<T> o) {
        observadores.add(o);
    }

    public void quitarObservador(ObservadorRepositorio<T> o) {
        observadores.remove(o);
    }

    private void notificar(EventoRepositorio<T> evento) {
        for (ObservadorRepositorio<T> o : observadores) {
            o.notificar(evento);
        }
    }

    public void crear(T entidad) {
        notificar(new EventoRepositorio<>(
                TipoEventoRepositorio.CREADO,
                entidad,
                new Date()
        ));
    }

    public void actualizar(T entidad) {
        notificar(new EventoRepositorio<>(
                TipoEventoRepositorio.ACTUALIZADO,
                entidad,
                new Date()
        ));
    }

    public void eliminar(T entidad) {
        notificar(new EventoRepositorio<>(
                TipoEventoRepositorio.ELIMINADO,
                entidad,
                new Date()
        ));
    }
}
    