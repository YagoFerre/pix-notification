package yago.ferreira.api.adapters.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import yago.ferreira.api.adapters.out.dto.request.NotificationRequest;
import yago.ferreira.api.adapters.out.dto.response.NotificationResponse;
import yago.ferreira.api.adapters.out.entity.NotificationEntity;
import yago.ferreira.api.domain.common.BaseMapper;
import yago.ferreira.api.domain.model.NotificationModel;

@Mapper
public interface NotificationMapper extends BaseMapper<NotificationEntity, NotificationModel, NotificationRequest, NotificationResponse> {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);
}
