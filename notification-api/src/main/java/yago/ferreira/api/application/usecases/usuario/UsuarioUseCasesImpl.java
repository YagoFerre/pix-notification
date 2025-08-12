package yago.ferreira.api.application.usecases.usuario;

import org.springframework.stereotype.Component;
import yago.ferreira.api.domain.model.UsuarioModel;
import yago.ferreira.api.domain.port.in.usecases.UsuarioUseCases;
import yago.ferreira.api.domain.port.out.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class UsuarioUseCasesImpl implements UsuarioUseCases {
    private final UsuarioRepository usuarioRepository;

    public UsuarioUseCasesImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioModel executeCreate(UsuarioModel domainModel) {
        domainModel.setCreatedAt(LocalDateTime.now());
        return usuarioRepository.executeSave(domainModel);
    }

    @Override
    public Optional<UsuarioModel> executeFindUser(Long id) {
        return usuarioRepository.executeFindUserById(id);
    }

}
