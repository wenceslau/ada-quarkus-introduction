package com.ada.pedido;

import com.ada.pedido.repository.ClienteRepository;
import com.ada.pedido.repository.entities.Cliente;
import com.ada.pedido.repository.entities.TipoUsuarioEnum;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

@Singleton
public class Startup {

    @Transactional
    public void criarAdmin(@Observes StartupEvent evt, ClienteRepository repository) {
        if (repository.buscarPorEmail("admin@ada.com").isEmpty()) {
            Cliente admin = new Cliente();
            admin.setNome("Admin");
            admin.setEmail("admin@ada.com");
            admin.setSenha(BcryptUtil.bcryptHash("12345678"));
            admin.setTipoUsuario(TipoUsuarioEnum.ADMIN);
            repository.persist(admin);
        }
    }
}
