package com.ada.pedido.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ClienteRepository implements PanacheRepositoryBase<Cliente, Long> {

    public Optional<Cliente> findByEmail(String email) {
        return find("email", email)
                .firstResultOptional();
    }

}
