package yago.ferreira.notification.infra.config.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationCreatedListener {
    public static final String SENT_NOTIFICATION_QUEUE = "notification.v1.sent-notification";

    @RabbitListener
    public void onNotificationCreated() {}
}
