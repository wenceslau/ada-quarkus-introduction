package com.ada.pedido.services.pedido;

import com.ada.pedido.repository.ProdutoRepository;
import com.ada.pedido.repository.entities.ItemPedido;
import com.ada.pedido.repository.entities.Pedido;
import com.ada.pedido.repository.entities.Produto;
import com.ada.pedido.repository.entities.StatusPedido;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;

@Priority(30)
@ApplicationScoped
public class PedidoValidarEstoque implements ProcessarPedido {

    private final ProdutoRepository produtoRepository;

    public PedidoValidarEstoque(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    @Transactional
    public void processar(Pedido pedido) {
        System.out.println("Etapa Validar estoque");

        pedido.getItens().forEach(itemPedido -> validarEstoque(pedido, itemPedido));

    }

    private void validarEstoque(Pedido pedido, ItemPedido itemPedido) {
        Optional<Produto> byId = produtoRepository.findByIdOptional(itemPedido.getProduto().getId());

        if (byId.isEmpty()) {
            throw new PedidoException("Produto %s não encontrado".formatted(itemPedido.getProduto().getId()));
        }

        Produto produto = byId.get();
        if (produto.getEstoque() < itemPedido.getQuantidade()) {
            String mensagem = "Estoque Produto %s não disponível. Pedido cancelado!".formatted(produto.getDescricao());
            pedido.setStatus(StatusPedido.CANCELADO);
            pedido.setMensagemStatus(mensagem);
        }

    }

}
