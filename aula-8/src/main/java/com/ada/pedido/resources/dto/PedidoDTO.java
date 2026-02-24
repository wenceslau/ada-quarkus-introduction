package com.ada.pedido.resources.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoDTO(
        @NotNull
        List<ItemPedidoDTO> itens) {
}
