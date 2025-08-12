package yago.ferreira.api.adapters.in.service;

import org.springframework.stereotype.Service;
import yago.ferreira.api.adapters.out.dto.request.NotificationRequest;
import yago.ferreira.api.adapters.out.dto.response.NotificationResponse;
import yago.ferreira.api.adapters.out.mapper.NotificationMapper;
import yago.ferreira.api.domain.exceptions.RecordNotFoundException;
import yago.ferreira.api.domain.model.NotificationModel;
import yago.ferreira.api.domain.model.UsuarioModel;
import yago.ferreira.api.domain.port.in.usecases.NotificationUseCases;
import yago.ferreira.api.domain.port.in.usecases.UsuarioUseCases;

@Service
public class NotificationService {
    private final NotificationUseCases notificationUseCases;
    private final UsuarioUseCases usuarioUseCases;

    public NotificationService(NotificationUseCases notificationUseCases, UsuarioUseCases usuarioUseCases) {
        this.notificationUseCases = notificationUseCases;
        this.usuarioUseCases = usuarioUseCases;
    }

    public NotificationResponse sendNotification(NotificationRequest notificationRequest, Long senderId) {
        UsuarioModel usuarioModel = usuarioUseCases.executeFindUser(senderId).orElseThrow(() -> new RecordNotFoundException(senderId));

        NotificationModel notificationModel = NotificationMapper.INSTANCE.requestToModel(notificationRequest);
        notificationModel.setSender(usuarioModel);
        return NotificationMapper.INSTANCE.modelToResponse(notificationUseCases.executeSendNotification(notificationModel));
    }
}
