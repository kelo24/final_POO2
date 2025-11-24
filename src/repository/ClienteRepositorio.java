package repository;

import models.Cliente;

public class ClienteRepositorio extends RepositorioMemoria<Cliente, String> {

    @Override
    protected String obtenerId(Cliente entidad) {
        return entidad.getDni();
    }
}
