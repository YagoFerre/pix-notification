package yago.ferreira.api.adapters.in.service;

import org.springframework.stereotype.Service;
import yago.ferreira.api.adapters.out.dto.request.NotificationRequest;
import yago.ferreira.api.adapters.out.dto.response.NotificationResponse;
import yago.ferreira.api.adapters.out.mapper.NotificationMapper;
import yago.ferreira.api.domain.model.NotificationModel;
import yago.ferreira.api.domain.port.in.usecases.NotificationUseCases;

@Service
public class NotificationService {
    private final NotificationUseCases notificationUseCases;

    public NotificationService(NotificationUseCases notificationUseCases) {
        this.notificationUseCases = notificationUseCases;
    }

    public NotificationResponse sendNotification(NotificationRequest notificationRequest) {
        // todo - findUserById()??
        NotificationModel notificationModel = NotificationMapper.INSTANCE.requestToModel(notificationRequest);
        return NotificationMapper.INSTANCE.modelToResponse(notificationUseCases.executeSendNotification(notificationModel));
    }
}
