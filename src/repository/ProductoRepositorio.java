package repository;

import models.Producto;

public class ProductoRepositorio extends RepositorioMemoria<Producto, String> {

    @Override
    protected String obtenerId(Producto entidad) {
        return entidad.getSku();
    }
}
