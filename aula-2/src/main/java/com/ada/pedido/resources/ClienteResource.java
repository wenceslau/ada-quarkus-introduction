package com.ada.pedido.resources;

import com.ada.pedido.resources.dto.Cliente;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
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
    public Response create(Cliente cliente) {

        cliente.setId(clientes.size() + 1L);
        clientes.add(cliente);

        return Response
                .status(Response.Status.CREATED)
                .entity(cliente)
                .build();
    }

    @GET
    @Path("/{clienteId}")
    public Response findById(@PathParam("clienteId") Long id) {
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
    public Response findAll() {

        return Response
                .ok(clientes)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Cliente cliente) {

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
    public Response partialUpdate(@PathParam("id") Long id, Cliente cliente) {

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
    public Response delete(@PathParam("id") Long id) {

        clientes.removeIf(x -> x.getId().equals(id));

        return Response
                .noContent()
                .build();
    }

}
