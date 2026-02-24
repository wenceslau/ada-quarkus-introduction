package com.ada.pedido.resources.dto;

import com.ada.pedido.repository.entities.ItemPedido;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        String produtoDescricao,
        BigDecimal produtoPreco,
        Integer quantidade,
        BigDecimal totalItem) {

    public static ItemPedidoResponseDTO criarDeEntidade(ItemPedido itemPedido){
        return new ItemPedidoResponseDTO(
                itemPedido.getProduto().getDescricao(),
                itemPedido.getProduto().getPreco(),
                itemPedido.getQuantidade(),
                itemPedido.getPreco().multiply(BigDecimal.valueOf(itemPedido.getQuantidade()))
        );
    }

}





