package com.ada.pedido.resources;

import com.ada.pedido.resources.dto.ClienteDTO;
import com.ada.pedido.resources.dto.ClienteResponseDTO;
import com.ada.pedido.services.ClienteService;
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

    private final ClienteService clienteService;

    public ClienteResource(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @POST
    @PermitAll
    public Response criar(@Valid ClienteDTO clienteDTO) {

        var clienteCriado = clienteService.criarCliente(clienteDTO);

        return Response
                .status(Response.Status.CREATED)
                .entity(ClienteResponseDTO.criarDeEntidade(clienteCriado))
                .build();
    }

    @GET
    @RolesAllowed("ADMIN")
    @Path("/{clienteId}")
    public Response buscarPorId(@PathParam("clienteId") Long id) {

        var cliente = clienteService.buscarClientePorId(id);

        return Response
                .ok(ClienteResponseDTO.criarDeEntidade(cliente))
                .build();
    }

    @GET
    @RolesAllowed({"ADMIN"})
    public Response buscarTodos() {

        var listaClientes = clienteService.buscarTodos();

        var listaDTO = listaClientes
                .stream()
                .map(ClienteResponseDTO::criarDeEntidade)
                .toList();

        return Response
                .ok(listaDTO)
                .build();
    }

    @PUT
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, @Valid ClienteDTO clienteDTO) {

        var cliente = clienteService.atualizarCliente(id, clienteDTO);

        return Response
                .ok(ClienteResponseDTO.criarDeEntidade(cliente))
                .build();
    }

    @PATCH
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    public Response atualizacaoParcial(@PathParam("id") Long id, ClienteDTO clienteDTO) {

        var cliente = clienteService.atualizarClienteParcial(id, clienteDTO);

        return Response
                .ok(ClienteResponseDTO.criarDeEntidade(cliente))
                .build();
    }

    @DELETE
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {

        clienteService.deletarCliente(id);

        return Response
                .noContent()
                .build();
    }

}
