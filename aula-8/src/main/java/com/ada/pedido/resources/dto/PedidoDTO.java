package com.ada.pedido.resources.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoDTO(
        @NotNull
        @NotEmpty(message = "O pedido deve ter pelo menos um item")
        List<ItemPedidoDTO> itens) {
}
