package observer;

public interface ObservadorRepositorio<T> {
    void notificar(EventoRepositorio<T> evento);
}
