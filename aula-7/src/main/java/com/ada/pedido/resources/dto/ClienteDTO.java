package com.ada.pedido.resources.dto;

import com.ada.pedido.repository.entities.Cliente;
import com.ada.pedido.repository.entities.TipoUsuarioEnum;
import com.ada.pedido.security.PasswordUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClienteDTO(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank @Email(message = "Email inválido") String email,
        @NotBlank @Size(min = 8, max = 20) String senha,
        @NotNull TipoUsuarioEnum tipoUsuario) {

    public Cliente toEntity() {
        var cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setSenha(PasswordUtils.encode(senha));
        cliente.setTipoUsuario(tipoUsuario);
        return cliente;
    }

    public void copyTo(Cliente cliente) {
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setSenha(PasswordUtils.encode(senha));
        cliente.setTipoUsuario(tipoUsuario);
    }

    public void copyToButNotNull(Cliente cliente) {
        if (nome != null) {
            cliente.setNome(nome);
        }

        if (email != null) {
            cliente.setEmail(email);
        }

        if (senha != null){
            cliente.setSenha(PasswordUtils.encode(senha));
        }

        if(tipoUsuario != null){
            cliente.setTipoUsuario(tipoUsuario);
        }
    }

}
