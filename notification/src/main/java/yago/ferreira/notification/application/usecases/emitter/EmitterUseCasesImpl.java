package yago.ferreira.notification.application.usecases.emitter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import yago.ferreira.notification.domain.model.SseEmitterModel;
import yago.ferreira.notification.domain.port.out.usecases.SseEmitterUseCase;

@Component
public class EmitterUseCasesImpl implements SseEmitterUseCase {
    @Override
    public SseEmitter addEmitter(Long userId) {
        return null;
    }

    @Override
    public SseEmitterModel publishNotification(Long userId, SseEmitterModel sseEmitterModel) {
        return null;
    }
}
