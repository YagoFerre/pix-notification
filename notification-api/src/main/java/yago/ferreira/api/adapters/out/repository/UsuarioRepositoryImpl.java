package yago.ferreira.api.adapters.out.repository;

import org.springframework.stereotype.Component;
import yago.ferreira.api.adapters.out.entity.UsuarioEntity;
import yago.ferreira.api.adapters.out.mapper.UsuarioMapper;
import yago.ferreira.api.domain.model.UsuarioModel;
import yago.ferreira.api.domain.port.out.repository.UsuarioRepository;

import java.util.Optional;

@Component
public class UsuarioRepositoryImpl implements UsuarioRepository {
    private final JpaUsuarioRepository jpaUsuarioRepository;

    public UsuarioRepositoryImpl(JpaUsuarioRepository jpaUsuarioRepository) {
        this.jpaUsuarioRepository = jpaUsuarioRepository;
    }

    @Override
    public UsuarioModel executeSave(UsuarioModel domainModel) {
        UsuarioEntity usuarioEntity = UsuarioMapper.INSTANCE.toEntity(domainModel);
        return UsuarioMapper.INSTANCE.toDomainModel(jpaUsuarioRepository.save(usuarioEntity));
    }

    @Override
    public Optional<UsuarioModel> executeFindUserById(Long id) {
        Optional<UsuarioEntity> usuarioEntity = jpaUsuarioRepository.findById(id);
        return usuarioEntity.map(UsuarioMapper.INSTANCE::toDomainModel);
    }
}
