package yago.ferreira.notification.adapters.out.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import yago.ferreira.notification.adapters.in.service.EmitterService;

@Component
public class NotificationCreatedListener {
    public static final String SENT_NOTIFICATION_QUEUE = "notification.v1.sent-notification";

    private final EmitterService emitterService;

    public NotificationCreatedListener(EmitterService emitterService) {
        this.emitterService = emitterService;
    }

    @RabbitListener(queues = SENT_NOTIFICATION_QUEUE)
    public void onNotificationCreated() {}
}
