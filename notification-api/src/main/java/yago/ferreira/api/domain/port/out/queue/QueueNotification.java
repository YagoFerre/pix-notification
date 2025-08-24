package yago.ferreira.api.domain.port.out.queue;

import yago.ferreira.api.domain.model.NotificationModel;

public interface QueueNotification {
    void executePublishMessage(NotificationModel domainModel);
}
