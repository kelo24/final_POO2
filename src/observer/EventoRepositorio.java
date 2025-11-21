package observer;

import java.util.Date;

public class EventoRepositorio<T> {

    private final TipoEventoRepositorio tipo;
    private final T entidad;
    private final Date fecha;

    public EventoRepositorio(TipoEventoRepositorio tipo, T entidad, Date fecha) {
        this.tipo = tipo;
        this.entidad = entidad;
        this.fecha = fecha;
    }

    public TipoEventoRepositorio getTipo() {
        return tipo;
    }

    public T getEntidad() {
        return entidad;
    }

    public Date getFecha() {
        return fecha;
    }
}
