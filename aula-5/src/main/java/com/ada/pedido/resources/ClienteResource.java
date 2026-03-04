package com.ada.pedido.resources;

import com.ada.pedido.repository.Cliente;
import com.ada.pedido.repository.ClienteRepository;
import com.ada.pedido.resources.dto.ClienteDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;

@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteResource {

    private final ClienteRepository clienteRepository;

    public ClienteResource(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @POST
    @Transactional
    public Response çriar(@Valid ClienteDTO cliente) {

        Cliente entidade = cliente.criarEntidade();
        clienteRepository.persist(entidade);

        return Response
                .status(Response.Status.CREATED)
                .entity(ClienteDTO.criarDeEntidade(entidade))
                .build();
    }

    @GET
    @Path("/{clienteId}")
    public Response buscarPor(@PathParam("clienteId") Long id) {

        var clienteOptional = clienteRepository.findByIdOptional(id);

        if (clienteOptional.isEmpty()) {
            throw new RuntimeException("Cliente não existe");
        }
        return Response
                .ok(ClienteDTO.criarDeEntidade(clienteOptional.get()))
                .build();
    }

    @GET
    public Response buscarTodos() {

        var listPaginada = clienteRepository.findAll();

        var listDTO = new ArrayList<ClienteDTO>();
        for (Cliente cliente : listPaginada.list()) {
            listDTO.add(ClienteDTO.criarDeEntidade(cliente));
        }

        return Response
                .ok(listDTO)
                .build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, @Valid ClienteDTO clienteDTO) {

        var clienteOptional = clienteRepository.findByIdOptional(id);

        if (clienteOptional.isEmpty()) {
            throw new RuntimeException("Cliente não existe");
        }

        var clienteAhAtualizar = clienteOptional.get();
        clienteDTO.copiarParaEntidade(clienteAhAtualizar);

        clienteRepository.persist(clienteAhAtualizar);

        return Response
                .ok(ClienteDTO.criarDeEntidade(clienteAhAtualizar))
                .build();
    }

    @PATCH
    @Transactional
    @Path("/{id}")
    public Response atualizacaoParcial(@PathParam("id") Long id, ClienteDTO clienteDTO) {

        if (clienteDTO.nome() != null && clienteDTO.nome().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio!");
        }
        if (clienteDTO.email() != null && clienteDTO.email().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio!");
        }

        var clienteOptional = clienteRepository.findByIdOptional(id);

        if (clienteOptional.isEmpty()) {
            throw new RuntimeException("Cliente não existe");
        }
        var clienteAhAtualizar = clienteOptional.get();
        clienteDTO.copiarParaEntidadeNaoNulo(clienteAhAtualizar);

        clienteRepository.persist(clienteAhAtualizar);

        return Response
                .ok(ClienteDTO.criarDeEntidade(clienteAhAtualizar))
                .build();

    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {

        clienteRepository.deleteById(id);

        return Response
                .noContent()
                .build();
    }

}
