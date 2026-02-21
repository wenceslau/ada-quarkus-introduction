package com.ada.pedido.resources.dto;

import com.ada.pedido.repository.entities.Cliente;
import com.ada.pedido.repository.entities.TipoUsuario;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String email,
        TipoUsuario tipoUsuario) {

    public static ClienteResponseDTO criarDeEntidade(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTipoUsuario()
        );
    }

}
