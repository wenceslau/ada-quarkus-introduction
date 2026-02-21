package com.ada.pedido.resources;

import com.ada.pedido.repository.ClienteRepository;
import com.ada.pedido.resources.dto.ClienteDTO;
import com.ada.pedido.security.PasswordUtils;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.wildfly.security.util.PasswordUtil;

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
    @PermitAll  //Endpoint público
    @Transactional
    public Response create(@Valid ClienteDTO cliente) {

       clienteRepository.persist(cliente.toEntity());

        return Response
                .status(Response.Status.CREATED)
                .entity(cliente)
                .build();
    }

    @GET
    @Path("/{clienteId}")
    public Response findById(@PathParam("clienteId") Long id) {

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
    public Response findAll() {

        var listPaginada = clienteRepository.findAll();

        return Response
                .ok(listPaginada.list())
                .build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid ClienteDTO cliente) {

        var clienteOptional = clienteRepository.findByIdOptional(id);

        if (clienteOptional.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }

        var clienteAhAtualizar = clienteOptional.get();
        cliente.copyTo(clienteAhAtualizar);

        clienteRepository.persist(clienteAhAtualizar);

        return Response
                .ok(clienteAhAtualizar)
                .build();
    }

    @PATCH
    @Transactional
    @Path("/{id}")
    public Response partialUpdate(@PathParam("id") Long id, ClienteDTO cliente) {

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
        cliente.copyToButNotNull(clienteAhAtualizar);

        clienteRepository.persist(clienteAhAtualizar);

        return Response
                .ok(clienteAhAtualizar)
                .build();

    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {

        clienteRepository.deleteById(id);

        return Response
                .noContent()
                .build();
    }

}
