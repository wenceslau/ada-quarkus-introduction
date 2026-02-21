package com.ada.pedido.resources;

import com.ada.pedido.repository.Cliente;
import com.ada.pedido.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


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
    public Response criar(Cliente cliente) {

       clienteRepository.persist(cliente);

        return Response
                .status(Response.Status.CREATED)
                .entity(cliente)
                .build();
    }

    @GET
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
    public Response buscarTodos() {

        var listPaginada = clienteRepository.findAll();

        return Response
                .ok(listPaginada.list())
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizar(@PathParam("id") Long id, Cliente cliente) {

        var clienteOptional = clienteRepository.findByIdOptional(id);

        if (clienteOptional.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();

        }

        var clienteAhAtualizar = clienteOptional.get();
        clienteAhAtualizar.setNome(cliente.getNome());
        clienteAhAtualizar.setEmail(cliente.getEmail());

        clienteRepository.persist(clienteAhAtualizar);

        return Response
                .ok(clienteAhAtualizar)
                .build();
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    public Response atualizacaoParcial(@PathParam("id") Long id, Cliente cliente) {

        var clienteOptional = clienteRepository.findByIdOptional(id);

        if (clienteOptional.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
        var clienteAhAtualizar = clienteOptional.get();

        if (cliente.getNome() != null) {
            clienteAhAtualizar.setNome(cliente.getNome());
        }

        if (cliente.getEmail() != null) {
            clienteAhAtualizar.setEmail(cliente.getEmail());
        }

        clienteRepository.persist(clienteAhAtualizar);

        return Response
                .ok(clienteAhAtualizar)
                .build();

    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {

        clienteRepository.deleteById(id);

        return Response
                .noContent()
                .build();
    }

}
