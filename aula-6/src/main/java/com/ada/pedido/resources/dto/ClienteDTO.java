package com.ada.pedido.resources.dto;

import com.ada.pedido.repository.Cliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteDTO(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank @Email(message = "Email inválido") String email) {

    public Cliente toEntity() {
        var cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEmail(email);
        return cliente;
    }

}
