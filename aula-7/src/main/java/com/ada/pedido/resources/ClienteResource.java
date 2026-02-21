package com.ada.pedido.resources;

import com.ada.pedido.repository.ClienteRepository;
import com.ada.pedido.resources.dto.ClienteDTO;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Authenticated
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
    @PermitAll
    public Response criar(@Valid ClienteDTO cliente) {

       clienteRepository.persist(cliente.criarEntidade());

        return Response
                .status(Response.Status.CREATED)
                .entity(cliente)
                .build();
    }

    @GET
    @RolesAllowed("ADMIN")
    @Path("/{clienteId}")
    public Response buscarPorId(@PathParam("clienteId") Long id) {

        var clienteOptional = clienteRepository.findByIdOptional(id);

        if (clienteOptional.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();

        }
        return Response
                .ok(clienteOptional.get())
                .build();
    }

    @GET
    @RolesAllowed({"ADMIN"})
    public Response buscarTodos() {

        var listaPaginada = clienteRepository.findAll();

        var list = listaPaginada.list()
                .stream()
                .map(ClienteDTO::criarDeEntidade)
                .toList();

        return Response
                .ok(list)
                .build();
    }

    @PUT
    @RolesAllowed("ADMIN")
    @Transactional
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, @Valid ClienteDTO cliente) {

        var clienteOptional = clienteRepository.findByIdOptional(id);

        if (clienteOptional.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }

        var clienteAhAtualizar = clienteOptional.get();
        cliente.copiarParaEntidade(clienteAhAtualizar);

        clienteRepository.persist(clienteAhAtualizar);

        return Response
                .ok(clienteAhAtualizar)
                .build();
    }

    @PATCH
    @RolesAllowed("ADMIN")
    @Transactional
    @Path("/{id}")
    public Response atualizacaoParcial(@PathParam("id") Long id, ClienteDTO cliente) {

        if (cliente.nome() != null && cliente.nome().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio!");
        }
        if (cliente.email() != null && cliente.email().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio!");
        }

        var clienteOptional = clienteRepository.findByIdOptional(id);

        if (clienteOptional.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
        var clienteAhAtualizar = clienteOptional.get();
        cliente.copiarParaEntidadeNaoNulo(clienteAhAtualizar);

        clienteRepository.persist(clienteAhAtualizar);

        return Response
                .ok(clienteAhAtualizar)
                .build();

    }

    @DELETE
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {

        clienteRepository.deleteById(id);

        return Response
                .noContent()
                .build();
    }

}
