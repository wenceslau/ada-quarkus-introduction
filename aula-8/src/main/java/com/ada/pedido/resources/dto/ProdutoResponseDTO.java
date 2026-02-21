package com.ada.pedido.resources.dto;

import com.ada.pedido.repository.entities.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
        Long id,
        String descricao,
        BigDecimal preco,
        Integer estoque
) {

    public static ProdutoResponseDTO criarDeEntidade(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getEstoque()
        );
    }

}
