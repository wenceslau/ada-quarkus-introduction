package com.ada.pedido.services;

import com.ada.pedido.repository.ProdutoRepository;
import com.ada.pedido.repository.entities.Cliente;
import com.ada.pedido.repository.entities.Produto;
import com.ada.pedido.resources.dto.ProdutoDTO;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Produto criarProduto(ProdutoDTO produtoDTO) {

        var produto = produtoDTO.criarEntidade();
        validarProduto(produto);
        produtoRepository.persist(produto);
        return produto;

    }

    @Transactional
    public Produto atualizarProduto(Long id, ProdutoDTO produtoDTO) {

        var produto = buscarProdutoPorId(id);
        produtoDTO.copiarParaEntidade(produto);

        validarProduto(produto);
        produtoRepository.persist(produto);

        return produto;

    }

    @Transactional
    public Produto atualizarProdutoParcial(Long id, ProdutoDTO produtoDTO) {

        var produto = buscarProdutoPorId(id);
        produtoDTO.copiarParaEntidadeNaoNulo(produto);

        validarProduto(produto);
        produtoRepository.persist(produto);

        return produto;

    }

    public Produto buscarProdutoPorId(Long id) {
        return produtoRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado!"));
    }

    public List<Produto> buscarProdutoPorDescricao(String filtro){
        return produtoRepository.findByDescricaoLikeIgnoreCase(filtro);
    }

    public List<Produto> buscarTodos() {
        return produtoRepository.listAll();
    }

    private static void validarProduto(Produto produto) {
        if (produto.getDescricao() == null || produto.getDescricao().isEmpty()){
            throw new IllegalArgumentException("Descrição não pode ser vazia!");
        }
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Preço deve ser maior que zero!");
        }
        if (produto.getEstoque() == null || produto.getEstoque() < 0){
            throw new IllegalArgumentException("Estoque deve ser maior ou igual a zero!");
        }
    }

}
