package com.ada.pedido.resources.dto;

import com.ada.pedido.repository.entities.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        LocalDateTime data,
        String cliente,
        String status,
        List<ItemPedidoResponseDTO> itens,
        BigDecimal totalPedido) {

    public static PedidoResponseDTO criarDeEntidade(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        var totalPedido = BigDecimal.ZERO;

        for (var item : pedido.getItens()) {
            totalPedido = totalPedido.add(
                    item.getPreco().multiply(
                            BigDecimal.valueOf(item.getQuantidade())
                    )
            );
        }

        return new PedidoResponseDTO(
                pedido.getDataPedido(),
                pedido.getCliente().getNome(),
                pedido.getStatus().name(),
                pedido.getItens().stream()
                        .map(ItemPedidoResponseDTO::criarDeEntidade)
                        .toList(),
                totalPedido);

    }


}
