package com.ada.pedido.resources;

import com.ada.pedido.resources.dto.PedidoDTO;
import com.ada.pedido.resources.dto.PedidoResponseDTO;
import com.ada.pedido.services.PedidoService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Authenticated
@Path("/pedidos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PedidoResource {

    private final PedidoService pedidoService;

    public PedidoResource(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @POST
    @RolesAllowed({"CLIENTE", "ADMIN"})
    public Response realizarPedido(@Valid PedidoDTO pedidoDTO) {

        var pedido = pedidoService.create(pedidoDTO);

        return Response
                .status(Response.Status.CREATED)
                .entity(PedidoResponseDTO.criarDeEntidade(pedido))
                .build();
    }

    @GET
    @RolesAllowed({"CLIENTE", "ADMIN"})
    public Response listarPedidos() {

        var pedidos = pedidoService.listarTodos();

        var pedidosResponse = pedidos.stream()
                .map(PedidoResponseDTO::criarDeEntidade)
                .toList();

        return Response
                .status(Response.Status.OK)
                .entity(pedidosResponse)
                .build();
    }

}
