package com.ada.pedido.repository;

import com.ada.pedido.repository.entities.Produto;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProdutoRepository implements PanacheRepositoryBase<Produto, Long> {

    public List<Produto> findByDescricaoLikeIgnoreCase(String filtro) {
        return find("lower(descricao) like lower(concat('%', ?1, '%'))", filtro)
                .list();
    }


}
