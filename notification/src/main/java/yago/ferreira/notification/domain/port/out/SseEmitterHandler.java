package yago.ferreira.notification.domain.port.out;

import yago.ferreira.notification.adapters.out.dto.response.SseEmitterResponse;

/**
 * Ao escutar a fila do RabbitMQ essa func retorna pro Sse a notificacao
 */
public interface SseEmitterHandler {
    void publishNotification(SseEmitterResponse emitterResponse);
}
