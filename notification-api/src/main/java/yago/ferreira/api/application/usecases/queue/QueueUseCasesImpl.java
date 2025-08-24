package yago.ferreira.api.application.usecases.queue;

import org.springframework.stereotype.Component;
import yago.ferreira.api.domain.model.NotificationModel;
import yago.ferreira.api.domain.port.in.usecases.QueueUseCases;
import yago.ferreira.api.domain.port.out.queue.QueueNotification;

@Component
public class QueueUseCasesImpl implements QueueUseCases {
    private final QueueNotification queueNotification;

    public QueueUseCasesImpl(QueueNotification queueNotification) {
        this.queueNotification = queueNotification;
    }

    @Override
    public void executeConvertAndSend(NotificationModel domainModel) {
        queueNotification.executePublishMessage(domainModel);
    }
}
