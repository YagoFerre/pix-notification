package yago.ferreira.api.adapters.in.service;

import org.springframework.stereotype.Service;
import yago.ferreira.api.adapters.out.dto.request.UsuarioRequest;
import yago.ferreira.api.adapters.out.dto.response.UsuarioResponse;
import yago.ferreira.api.adapters.out.mapper.UsuarioMapper;
import yago.ferreira.api.domain.model.UsuarioModel;
import yago.ferreira.api.domain.port.in.usecases.UsuarioUseCases;

@Service
public class UsuarioService {
    private final UsuarioUseCases usuarioUseCases;

    public UsuarioService(UsuarioUseCases usuarioUseCases) {
        this.usuarioUseCases = usuarioUseCases;
    }

    public UsuarioResponse createUsuario(UsuarioRequest usuarioRequest) {
        UsuarioModel usuarioModel = UsuarioMapper.INSTANCE.requestToModel(usuarioRequest);
        return UsuarioMapper.INSTANCE.modelToResponse(usuarioUseCases.executeCreate(usuarioModel));
    };
}
