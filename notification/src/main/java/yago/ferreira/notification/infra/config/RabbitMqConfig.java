package yago.ferreira.notification.infra.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String SENT_NOTIFICATION_QUEUE = "notification.v1.sent-notification";

    /**
     * cria uma fila
     */
    @Bean
    public Queue queue() {
        return new Queue(SENT_NOTIFICATION_QUEUE);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * ApplicationListener<Type> -> componente que ouve/reage as eventos
     * do ciclo de vida do Spring Boot
     */
    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }
}
