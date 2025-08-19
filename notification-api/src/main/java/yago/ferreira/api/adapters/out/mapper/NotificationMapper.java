package yago.ferreira.api.adapters.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import yago.ferreira.api.adapters.out.dto.request.NotificationRequest;
import yago.ferreira.api.adapters.out.dto.response.NotificationResponse;
import yago.ferreira.api.adapters.out.entity.NotificationEntity;
import yago.ferreira.api.domain.common.BaseMapper;
import yago.ferreira.api.domain.model.NotificationModel;
import yago.ferreira.api.domain.model.UsuarioModel;

import java.time.LocalDateTime;

@Mapper
public interface NotificationMapper extends BaseMapper<NotificationEntity, NotificationModel, NotificationRequest, NotificationResponse> {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    default NotificationModel bindWithUserRequestToModel(NotificationRequest notificationRequest, UsuarioModel usuarioModel) {
        if (notificationRequest == null || usuarioModel == null) {
            return null;
        }

        NotificationModel notificationModel = requestToModel(notificationRequest);

        notificationModel.setSender(usuarioModel);
        notificationModel.setCreatedAt(LocalDateTime.now());
        return notificationModel;
    }

}
