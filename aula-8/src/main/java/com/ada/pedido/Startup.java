package com.ada.pedido;

import com.ada.pedido.repository.ClienteRepository;
import com.ada.pedido.repository.ProdutoRepository;
import com.ada.pedido.repository.entities.Cliente;
import com.ada.pedido.repository.entities.Produto;
import com.ada.pedido.repository.entities.TipoUsuario;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@Singleton
public class Startup {

    @Transactional
    public void criarAdmin(@Observes StartupEvent evt, ClienteRepository repository) {
        if (repository.buscarPorEmail("admin@ada.com").isEmpty()) {
            Cliente admin = new Cliente();
            admin.setNome("Admin");
            admin.setEmail("admin@ada.com");
            admin.setSenha(BcryptUtil.bcryptHash("12345678"));
            admin.setTipoUsuario(TipoUsuario.ADMIN);
            repository.persist(admin);
        }
    }

    @Transactional
    public void criarProduto(@Observes StartupEvent evt, ProdutoRepository repository) {
        if (repository.findByDescricaoLikeIgnoreCase("Produto para teste").isEmpty()) {
            Produto produto = new Produto();
            produto.setDescricao("Produto para teste");
            produto.setPreco(BigDecimal.TEN);
            produto.setEstoque(10);
            repository.persist(produto);
        }
    }
}
