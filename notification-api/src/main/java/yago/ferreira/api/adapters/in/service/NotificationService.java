package yago.ferreira.api.adapters.in.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import yago.ferreira.api.adapters.out.dto.request.NotificationRequest;
import yago.ferreira.api.adapters.out.dto.response.NotificationResponse;
import yago.ferreira.api.adapters.out.mapper.NotificationMapper;
import yago.ferreira.api.domain.exceptions.RecordNotFoundException;
import yago.ferreira.api.domain.model.NotificationModel;
import yago.ferreira.api.domain.model.UsuarioModel;
import yago.ferreira.api.domain.port.in.usecases.NotificationUseCases;
import yago.ferreira.api.domain.port.in.usecases.UsuarioUseCases;
import yago.ferreira.api.infra.config.RabbitMqConfig;

@Service
public class NotificationService {
    private final NotificationUseCases notificationUseCases;
    private final UsuarioUseCases usuarioUseCases;
    private final RabbitTemplate rabbitTemplate;

    public NotificationService(NotificationUseCases notificationUseCases, UsuarioUseCases usuarioUseCases, RabbitTemplate rabbitTemplate) {
        this.notificationUseCases = notificationUseCases;
        this.usuarioUseCases = usuarioUseCases;
        this.rabbitTemplate = rabbitTemplate;
    }

    public NotificationResponse sendNotification(NotificationRequest notificationRequest) {
        UsuarioModel usuarioModel = usuarioUseCases.executeFindUser(notificationRequest.getSenderId()).orElseThrow(() -> new RecordNotFoundException(notificationRequest.getSenderId()));
        NotificationModel notificationModel = NotificationMapper.INSTANCE.bindWithUserRequestToModel(notificationRequest, usuarioModel);
        NotificationResponse notificationCreatedResponse = NotificationMapper.INSTANCE.modelToResponse(notificationUseCases.executeSendNotification(notificationModel));

        rabbitTemplate.convertAndSend(RabbitMqConfig.SENT_NOTIFICATION_QUEUE, notificationCreatedResponse);
        return notificationCreatedResponse;
    }
}
