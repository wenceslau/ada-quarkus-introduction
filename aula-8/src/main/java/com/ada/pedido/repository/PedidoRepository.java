package com.ada.pedido.repository;

import com.ada.pedido.repository.entities.Pedido;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PedidoRepository implements PanacheRepositoryBase<Pedido, Long> {


}
