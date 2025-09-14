package yago.ferreira.notification.domain.port.out;

import yago.ferreira.notification.adapters.out.dto.response.SseEmitterResponse;

import java.io.IOException;

/**
 * Um abre conexao, outro vai servir de axilio
 * para o RabbitMq ao escutar a fila
 */
public interface SseEmitterHandler {
    SseEmitterResponse publishNotification(Long userId, SseEmitterResponse sseEmitterModel) throws IOException;
}
