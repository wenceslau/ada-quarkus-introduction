package com.ada.pedido.services;

import com.ada.pedido.repository.ClienteRepository;
import com.ada.pedido.repository.entities.Cliente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.client.Client;

import java.util.List;

@ApplicationScoped
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente salvarCliente(Cliente cliente) {
        validarCliente(cliente);
        clienteRepository.persist(cliente);
        return cliente;
    }

    public void deletarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public Cliente buscarClientePorId(Long id) {
        return clienteRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado!"));
    }

    public List<Cliente> buscarTodos(){
        return clienteRepository.listAll();
    }

    private static void validarCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio!");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio!");
        }
        if (cliente.getSenha() == null || cliente.getSenha().isEmpty() || cliente.getSenha().length() < 8) {
            throw new IllegalArgumentException("Senha não pode ser vazia e deve ter no mínimo 8 caracteres!");
        }
    }


}
