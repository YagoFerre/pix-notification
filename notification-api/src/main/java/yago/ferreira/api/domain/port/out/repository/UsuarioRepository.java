package yago.ferreira.api.domain.port.out.repository;

import yago.ferreira.api.domain.model.UsuarioModel;

import java.util.Optional;

public interface UsuarioRepository {
    UsuarioModel executeSave(UsuarioModel domainModel);
    Optional<UsuarioModel> executeFindUserById(Long id);
}
