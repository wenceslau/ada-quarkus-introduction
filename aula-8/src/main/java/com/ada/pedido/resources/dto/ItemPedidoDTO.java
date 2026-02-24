package com.ada.pedido.resources.dto;

import com.ada.pedido.repository.entities.ItemPedido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPedidoDTO(
        @NotNull
        Long produtoId,

        @NotNull
        @Positive
        Integer quantidade) {

}
