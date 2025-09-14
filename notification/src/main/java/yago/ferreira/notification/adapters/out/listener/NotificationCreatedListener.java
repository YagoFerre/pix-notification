package yago.ferreira.notification.adapters.out.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import yago.ferreira.notification.adapters.in.service.EmitterService;
import yago.ferreira.notification.adapters.out.dto.response.NotificationResponse;
import yago.ferreira.notification.adapters.out.dto.response.SseEmitterResponse;

import java.io.IOException;

@Component
public class NotificationCreatedListener {
    public static final String SENT_NOTIFICATION_QUEUE = "notification.v1.sent-notification";

    private final EmitterService emitterService;

    public NotificationCreatedListener(EmitterService emitterService) {
        this.emitterService = emitterService;
    }

    @RabbitListener(queues = SENT_NOTIFICATION_QUEUE)
    public void onNotificationCreated(NotificationResponse notificationRecived) throws IOException {
        SseEmitterResponse emitterResponse = new SseEmitterResponse();
        emitterResponse.setId(notificationRecived.getSender().getId());
        emitterResponse.setData(notificationRecived);
        emitterResponse.setEvent("pix-notification");

        emitterService.publishNotification(emitterResponse);
    }
}
