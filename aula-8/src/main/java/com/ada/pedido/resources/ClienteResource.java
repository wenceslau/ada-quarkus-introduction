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
    @Transactional
    public Response criar(@Valid ClienteDTO clienteDTO) {

        var clienteCriado = clienteService.salvarCliente(clienteDTO.criarEntidade());

        return Response
                .status(Response.Status.CREATED)
                .entity(ClienteResponseDTO.criarDeEntidade(clienteCriado))
                .build();
    }

    @GET
    @RolesAllowed("ADMIN")
    @Path("/{clienteId}")
    public Response buscarPorId(@PathParam("clienteId") Long id) {

        return Response
                .ok(clienteService.buscarClientePorId(id))
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
    @Transactional
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, @Valid ClienteDTO clienteDTO) {

        var clienteAhAtualizar = clienteService.buscarClientePorId(id);
        clienteDTO.copiarParaEntidade(clienteAhAtualizar);

        var clienteAtualizado = clienteService.salvarCliente(clienteAhAtualizar);

        return Response
                .ok(ClienteResponseDTO.criarDeEntidade(clienteAtualizado))
                .build();
    }

    @PATCH
    @Transactional
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    public Response atualizacaoParcial(@PathParam("id") Long id, ClienteDTO cliente) {

        var clienteAhAtualizar = clienteService.buscarClientePorId(id);

        cliente.copiarParaEntidadeNaoNulo(clienteAhAtualizar);

        var clienteAtualizado = clienteService.salvarCliente(clienteAhAtualizar);

        return Response
                .ok(ClienteResponseDTO.criarDeEntidade(clienteAtualizado))
                .build();
    }

    @DELETE
    @Transactional
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {

        clienteService.deletarCliente(id);

        return Response
                .noContent()
                .build();
    }

}
