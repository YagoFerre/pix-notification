package yago.ferreira.api.adapters.out.repository;

import org.springframework.stereotype.Component;
import yago.ferreira.api.adapters.out.entity.NotificationEntity;
import yago.ferreira.api.adapters.out.mapper.NotificationMapper;
import yago.ferreira.api.domain.model.NotificationModel;
import yago.ferreira.api.domain.port.out.repository.NotificationRepository;

@Component
public class NotificationRepositoryImpl implements NotificationRepository {
    private final JpaNotificationRepository jpaNotificationRepository;

    public NotificationRepositoryImpl(JpaNotificationRepository jpaNotificationRepository) {
        this.jpaNotificationRepository = jpaNotificationRepository;
    }

    @Override
    public NotificationModel executeSave(NotificationModel domainModel) {
        NotificationEntity notificationEntity = NotificationMapper.INSTANCE.toEntity(domainModel);
        return NotificationMapper.INSTANCE.toDomainModel(jpaNotificationRepository.save(notificationEntity));
    }
}
