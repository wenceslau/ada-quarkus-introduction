package com.ada.pedido.resources.dto;

import com.ada.pedido.repository.entities.Produto;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoDTO(
        @Size(max = 50, message = "Descrição deve ter no máximo 50 caracteres")
        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @PositiveOrZero(message = "Preço deve ser maior ou igual a zero")
        @NotNull(message = "Preço é obrigatório")
        BigDecimal preco,

        @PositiveOrZero(message = "Estoque deve ser maior ou igual a zero")
        @NotNull(message = "Estoque é obrigatório")
        Integer estoque
) {

    public Produto criarEntidade() {
        var produto = new Produto();
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setEstoque(estoque);
        return produto;
    }

    public void copiarParaEntidade(Produto produto) {
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setEstoque(estoque);
    }

    public void copiarParaEntidadeNaoNulo(Produto produto) {
        if (descricao != null) {
            produto.setDescricao(descricao);
        }
        if (preco != null) {
            produto.setPreco(preco);
        }
        if (estoque != null) {
            produto.setEstoque(estoque);
        }
    }

}
