package yago.ferreira.api.domain.port.in.usecases;

import yago.ferreira.api.domain.model.NotificationModel;

public interface QueueUseCases {
    void executeConvertAndSend(NotificationModel domainModel);
}
