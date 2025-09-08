package yago.ferreira.notification.adapters.out.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationCreatedListener {
    public static final String SENT_NOTIFICATION_QUEUE = "notification.v1.sent-notification";

    @RabbitListener(queues = SENT_NOTIFICATION_QUEUE)
    public void onNotificationCreated() {}
}
