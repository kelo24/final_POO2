package repository;

import java.util.*;
import observer.EventoRepositorio;
import observer.TipoEventoRepositorio;
import observer.ObservadorRepositorio;

public abstract class RepositorioMemoria<T, ID> implements CrudRepository<T, ID> {

    protected final Map<ID, T> almacenamiento = new HashMap<>();
    private final List<ObservadorRepositorio<T>> observadores = new ArrayList<>();

    // --- Métodos para Observer ---
    public void agregarObservador(ObservadorRepositorio<T> observador) {
        observadores.add(observador);
    }

    public void eliminarObservador(ObservadorRepositorio<T> observador) {
        observadores.remove(observador);
    }

    protected void notificar(TipoEventoRepositorio tipo, T entidad) {
        EventoRepositorio<T> evento = new EventoRepositorio<>(tipo, entidad, new Date());
        for (ObservadorRepositorio<T> obs : observadores) {
            obs.notificar(evento);
        }
    }

    // --- Métodos abstractos para manejar ID ---
    protected abstract ID obtenerId(T entidad);

    // --- Implementación CRUD base ---
    @Override
    public void guardar(T entidad) {
        ID id = obtenerId(entidad);
        almacenamiento.put(id, entidad);
        notificar(TipoEventoRepositorio.CREADO, entidad);
    }

    @Override
    public void actualizar(T entidad) {
        ID id = obtenerId(entidad);
        if (almacenamiento.containsKey(id)) {
            almacenamiento.put(id, entidad);
            notificar(TipoEventoRepositorio.ACTUALIZADO, entidad);
        } else {
            
            guardar(entidad);
        }
    }

    @Override
    public void eliminar(ID id) {
        T entidad = almacenamiento.remove(id);
        if (entidad != null) {
            notificar(TipoEventoRepositorio.ELIMINADO, entidad);
        }
    }

    @Override
    public Optional<T> buscarPorId(ID id) {
        return Optional.ofNullable(almacenamiento.get(id));
    }

    @Override
    public List<T> obtenerTodos() {
        return new ArrayList<>(almacenamiento.values());
    }
}
