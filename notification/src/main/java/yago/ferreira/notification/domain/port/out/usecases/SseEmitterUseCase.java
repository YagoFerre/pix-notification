package yago.ferreira.notification.domain.port.out.usecases;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import yago.ferreira.notification.domain.model.SseEmitterModel;

/**
 * Um abre conexao, outro vai servir de axilio
 * para o RabbitMq ao escutar a fila
 */
public interface SseEmitterUseCase {
    SseEmitter addEmitter(Long userId);
    void publishNotification(Long userId, SseEmitterModel sseEmitterModel);
}
