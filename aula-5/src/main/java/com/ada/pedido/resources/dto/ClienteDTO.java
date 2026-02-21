package com.ada.pedido.resources.dto;

import com.ada.pedido.repository.Cliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteDTO(
        @NotBlank(message = "Nome é obrigatório") String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido") String email
) {


    public Cliente criarEntidade() {
        var cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEmail(email);
        return cliente;
    }

    public void copiarParaEntidade(Cliente cliente) {
        cliente.setNome(nome);
        cliente.setEmail(email);
    }

    public void copiarParaEntidadeNaoNulo(Cliente cliente) {
        if (nome != null) {
            cliente.setNome(nome);
        }

        if (email != null) {
            cliente.setEmail(email);
        }

    }

    public static ClienteDTO criarDeEntidade(Cliente cliente) {
        return new ClienteDTO(
                cliente.getNome(),
                cliente.getEmail()
        );
    }

}
