package repository;

import models.Pedido;

public class PedidoRepositorio extends RepositorioMemoria<Pedido, String> {

    @Override
    protected String obtenerId(Pedido entidad) {
        return entidad.getIdPedido();
    }
}
