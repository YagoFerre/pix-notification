package yago.ferreira.notification.adapters.in.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import yago.ferreira.notification.domain.port.out.usecases.SseEmitterUseCase;

@Service
public class EmitterService {
    private final SseEmitterUseCase sseEmitterUseCase;

    public EmitterService(SseEmitterUseCase sseEmitterUseCase) {
        this.sseEmitterUseCase = sseEmitterUseCase;
    }

    public SseEmitter getEmitterClient(Long userId) {
        return sseEmitterUseCase.addEmitter(userId);
    };


}
