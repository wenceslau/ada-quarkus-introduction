package com.ada.pedido.resources;

import com.ada.pedido.resources.dto.ProdutoDTO;
import com.ada.pedido.resources.dto.ProdutoResponseDTO;
import com.ada.pedido.services.ProdutoService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Authenticated
@Path("/produtos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProdutoResource {

    private final ProdutoService produtoService;

    public ProdutoResource(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response criar(@Valid ProdutoDTO produtoDTO) {

        var produtoCriado = produtoService.criarProduto(produtoDTO);

        return Response
                .status(Response.Status.CREATED)
                .entity(ProdutoResponseDTO.criarDeEntidade(produtoCriado))
                .build();
    }

    @PUT
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, @Valid ProdutoDTO produtoDTO) {

        var produto = produtoService.atualizarProduto(id, produtoDTO);

        return Response
                .ok(ProdutoResponseDTO.criarDeEntidade(produto))
                .build();
    }

    @PATCH
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    public Response atualizacaoParcial(@PathParam("id") Long id, ProdutoDTO produtoDTO) {

        var produto = produtoService.atualizarProdutoParcial(id, produtoDTO);

        return Response
                .ok(ProdutoResponseDTO.criarDeEntidade(produto))
                .build();
    }

    @GET
    @RolesAllowed({"ADMIN", "CLIENTE"})
    public Response listar() {
        var listaDTO = produtoService.buscarTodos()
                .stream()
                .map(ProdutoResponseDTO::criarDeEntidade)
                .toList();

        return Response
                .ok(listaDTO)
                .build();
    }

    @GET
    @RolesAllowed({"ADMIN", "CLIENTE"})
    @Path("/{produtoId}")
    public Response buscarPorId(@PathParam("produtoId") Long id) {
        return Response
                .ok(ProdutoResponseDTO.criarDeEntidade(produtoService.buscarProdutoPorId(id)))
                .build();
    }

    @GET
    @RolesAllowed({"ADMIN", "CLIENTE"})
    @Path("/procurar/{descricao}")
    public Response buscarPorDescricao(@PathParam("descricao") String filtro) {
        var listaDTO = produtoService.buscarProdutoPorDescricao(filtro)
                .stream()
                .map(ProdutoResponseDTO::criarDeEntidade)
                .toList();

        return Response
                .ok(listaDTO)
                .build();
    }

}
