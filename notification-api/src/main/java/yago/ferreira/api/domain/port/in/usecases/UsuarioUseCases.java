package yago.ferreira.api.domain.port.in.usecases;

import yago.ferreira.api.domain.model.UsuarioModel;

import java.util.Optional;

public interface UsuarioUseCases {
    UsuarioModel executeCreate(UsuarioModel domainModel);
    Optional<UsuarioModel> executeFindUser(Long id);
}
