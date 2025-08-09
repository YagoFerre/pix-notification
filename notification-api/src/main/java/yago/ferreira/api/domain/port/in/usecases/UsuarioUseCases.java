package yago.ferreira.api.domain.port.in.usecases;

import yago.ferreira.api.domain.model.UsuarioModel;

public interface UsuarioUseCases {
    UsuarioModel executeCreate(UsuarioModel domainModel);
}
