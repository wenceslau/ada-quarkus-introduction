package com.ada.pedido.services;

import com.ada.pedido.repository.ClienteRepository;
import com.ada.pedido.repository.PedidoRepository;
import com.ada.pedido.repository.ProdutoRepository;
import com.ada.pedido.repository.entities.ItemPedido;
import com.ada.pedido.repository.entities.Pedido;
import com.ada.pedido.repository.entities.StatusPedido;
import com.ada.pedido.resources.dto.ItemPedidoDTO;
import com.ada.pedido.resources.dto.PedidoDTO;
import com.ada.pedido.resources.exception.PedidoException;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class PedidoService {

    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;
    private final SecurityIdentity securityIdentity;

    public PedidoService(ProdutoRepository produtoRepository,
                         ClienteRepository clienteRepository,
                         PedidoRepository pedidoRepository,
                         SecurityIdentity securityIdentity) {
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
        this.securityIdentity = securityIdentity;
    }

    @Transactional
    public Pedido create(PedidoDTO pedidoDTO) {

        var clienteEmail = securityIdentity.getPrincipal().getName();
        var cliente = clienteRepository
                .buscarPorEmail(clienteEmail)
                .orElseThrow(() -> new PedidoException("Usuario não encontrado!"));

        var pedido = new Pedido();
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.NOVO);
        pedido.setMensagemStatus("");
        pedido.setCliente(cliente);

        var itens = pedidoDTO.itens().stream()
                .map(itemPedidoRequest -> contruirItemPedido(itemPedidoRequest, pedido))
                .toList();
        pedido.setItens(itens);

        // TODO implement processing 'pedido'
        pedido.setStatus(StatusPedido.PROCESSADO);

        if (pedido.getStatus().equals(StatusPedido.CANCELADO)) {
            throw new PedidoException("Pedido não pode ser realizado! " + pedido.getMensagemStatus());
        }

        this.pedidoRepository.persist(pedido);

        return pedido;

    }

    private ItemPedido contruirItemPedido(ItemPedidoDTO itemPedidoDTO, Pedido pedido) {

        var produto = produtoRepository
                .findByIdOptional(itemPedidoDTO.produtoId())
                .orElseThrow(() -> new PedidoException("Produto não encontrado! Id: " + itemPedidoDTO.produtoId()));

        var itemPedido = new ItemPedido();
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(itemPedidoDTO.quantidade());
        itemPedido.setPedido(pedido);
        itemPedido.setPreco(produto.getPreco());

        return itemPedido;
    }

    public List<Pedido> listarTodos() {
        return this.pedidoRepository.listAll();
    }
}
