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
    @Transactional
    @RolesAllowed("ADMIN")
    public Response criar(@Valid ProdutoDTO produtoDTO) {

        var produto = produtoDTO.criarEntidade();

        var produtoCriado = produtoService.salvarProduto(produto);

        return Response
                .status(Response.Status.CREATED)
                .entity(ProdutoResponseDTO.criarDeEntidade(produtoCriado))
                .build();
    }

    @PUT
    @Transactional
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, @Valid ProdutoDTO produtoDTO) {

        var ahAtualizar = produtoService.buscarProdutoPorId(id);
        produtoDTO.copiarParaEntidade(ahAtualizar);

        var atualizado = produtoService.salvarProduto(ahAtualizar);

        return Response
                .ok(ProdutoResponseDTO.criarDeEntidade(atualizado))
                .build();
    }

    @PATCH
    @Transactional
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    public Response atualizacaoParcial(@PathParam("id") Long id, ProdutoDTO produtoDTO) {

        var ahAtualizar = produtoService.buscarProdutoPorId(id);
        produtoDTO.copiarParaEntidadeNaoNulo(ahAtualizar);

        var atualizado = produtoService.salvarProduto(ahAtualizar);

        return Response
                .ok(ProdutoResponseDTO.criarDeEntidade(atualizado))
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
