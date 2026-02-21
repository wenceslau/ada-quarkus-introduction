package com.ada.pedido.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteResource {

    private final List<Cliente> clientes = new ArrayList<>();

    @POST
    public Response criar(Cliente cliente) {

        cliente.setId(clientes.size() + 1L);
        clientes.add(cliente);

        return Response
                .status(Response.Status.CREATED)
                .entity(cliente)
                .build();
    }

    @GET
    @Path("/{clienteId}")
    public Response buscarPorId(@PathParam("clienteId") Long id) {
        var cliente = clientes.stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();

        if (cliente.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();

        }
        return Response
                .ok(cliente.get())
                .build();
    }

    @GET
    public Response buscarTodos() {

        return Response
                .ok(clientes)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, Cliente cliente) {

        var clienteOptional = clientes.stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();

        if (clienteOptional.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();

        }

        var clienteAhAtualizar = clienteOptional.get();
        clienteAhAtualizar.setNome(cliente.getNome());
        clienteAhAtualizar.setEmail(cliente.getEmail());

        return Response
                .ok(clienteAhAtualizar)
                .build();
    }

    @PATCH
    @Path("/{id}")
    public Response atualizacaoParcial(@PathParam("id") Long id, Cliente cliente) {

        var clienteOptional = clientes.stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();

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

        return Response
                .ok(clienteAhAtualizar)
                .build();

    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {

        clientes.removeIf(x -> x.getId().equals(id));

        return Response
                .noContent()
                .build();
    }

}
