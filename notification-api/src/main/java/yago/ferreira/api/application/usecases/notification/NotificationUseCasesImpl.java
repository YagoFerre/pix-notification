package yago.ferreira.api.application.usecases.notification;

import org.springframework.stereotype.Component;
import yago.ferreira.api.domain.model.NotificationModel;
import yago.ferreira.api.domain.port.in.usecases.NotificationUseCases;
import yago.ferreira.api.domain.port.out.repository.NotificationRepository;

@Component
public class NotificationUseCasesImpl implements NotificationUseCases {
    private final NotificationRepository notificationRepository;

    public NotificationUseCasesImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public NotificationModel executeSendNotification(NotificationModel notificationModel) {
        return notificationRepository.executeSave(notificationModel);
    }
}
