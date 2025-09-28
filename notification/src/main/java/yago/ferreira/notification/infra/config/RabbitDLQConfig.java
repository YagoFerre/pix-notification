package yago.ferreira.notification.infra.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitDLQConfig {
    public static final String SENT_NOTIFICATION_QUEUE = "notification.v1.sent-notification";
    public static final String DLX_EXCHANGE = "dead.letter.exchange";
    public static final String DLQ_QUEUE = "dead.letter.queue";
}
