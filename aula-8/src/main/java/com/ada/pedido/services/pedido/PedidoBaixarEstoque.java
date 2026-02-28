package com.ada.pedido.services.pedido;

import com.ada.pedido.repository.PedidoRepository;
import com.ada.pedido.repository.ProdutoRepository;
import com.ada.pedido.repository.entities.ItemPedido;
import com.ada.pedido.repository.entities.Pedido;
import com.ada.pedido.repository.entities.Produto;
import com.ada.pedido.repository.entities.StatusPedido;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;

@Priority(20)
@ApplicationScoped
public class PedidoBaixarEstoque implements ProcessarPedido {

    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    public PedidoBaixarEstoque(ProdutoRepository produtoRepository, PedidoRepository pedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    @Transactional
    public void processar(Pedido pedido) {
        System.out.println("Etapa Baixar estoque");

        if (StatusPedido.CANCELADO.equals(pedido.getStatus())) {
            return;
        }

        // Atualiza o estoque dos produtos do pedido
        pedido.getItens().forEach(this::atualizarEstoque);
        pedido.setStatus(StatusPedido.PROCESSADO);
        pedidoRepository.persist(pedido);

    }

    private void atualizarEstoque(ItemPedido itemPedido) {
        // Localiza o produto no banco de dados
        Optional<Produto> byId = produtoRepository.findByIdOptional(itemPedido.getProduto().getId());

        // Verifica se o produto existe e atualiza o estoque
        if (byId.isPresent()) {
            Produto produto = byId.get();
            produto.setEstoque(produto.getEstoque() - itemPedido.getQuantidade());
            produtoRepository.persist(produto);
        }
    }
}
