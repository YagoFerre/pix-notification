package yago.ferreira.api.domain.port.out.repository;

import yago.ferreira.api.domain.model.UsuarioModel;

public interface UsuarioRepository {
    UsuarioModel executeSave(UsuarioModel domainModel);
}
