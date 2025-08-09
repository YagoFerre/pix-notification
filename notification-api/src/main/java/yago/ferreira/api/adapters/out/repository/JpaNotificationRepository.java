package yago.ferreira.api.adapters.out.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yago.ferreira.api.adapters.out.entity.NotificationEntity;

@Repository
public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, Long> {
}
