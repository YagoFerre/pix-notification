package yago.ferreira.notification.domain.port.out.usecases;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import yago.ferreira.notification.domain.model.SseEmitterModel;

import java.io.IOException;

/**
 * Um abre conexao, outro vai servir de axilio
 * para o RabbitMq ao escutar a fila
 */
public interface SseEmitterUseCase {
    SseEmitter addEmitter(Long userId);
    SseEmitterModel publishNotification(Long userId, SseEmitterModel sseEmitterModel) throws IOException;
}
