package yago.ferreira.api.adapters.out.queue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import yago.ferreira.api.adapters.out.mapper.NotificationMapper;
import yago.ferreira.api.domain.model.NotificationModel;
import yago.ferreira.api.domain.port.out.queue.QueueNotification;
import yago.ferreira.api.infra.config.RabbitMqConfig;

@Component
public class QueueNotificationImpl implements QueueNotification {
    private final RabbitTemplate rabbitTemplate;

    public QueueNotificationImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void executePublishMessage(NotificationModel domainModel) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.SENT_NOTIFICATION_QUEUE, NotificationMapper.INSTANCE.modelToResponse(domainModel));
    }
}
