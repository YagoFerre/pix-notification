package yago.ferreira.api.domain.port.out.repository;

import yago.ferreira.api.domain.model.NotificationModel;

public interface NotificationRepository {
    NotificationModel executeSave(NotificationModel domainModel);
}
