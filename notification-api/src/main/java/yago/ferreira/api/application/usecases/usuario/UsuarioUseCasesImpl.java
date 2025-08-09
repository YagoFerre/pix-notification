package yago.ferreira.api.application.usecases.usuario;

import org.springframework.stereotype.Component;
import yago.ferreira.api.domain.model.UsuarioModel;
import yago.ferreira.api.domain.port.in.usecases.UsuarioUseCases;
import yago.ferreira.api.domain.port.out.repository.UsuarioRepository;

@Component
public class UsuarioUseCasesImpl implements UsuarioUseCases {
    private final UsuarioRepository usuarioRepository;

    public UsuarioUseCasesImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioModel executeCreate(UsuarioModel domainModel) {
        return usuarioRepository.executeSave(domainModel);
    }
}
