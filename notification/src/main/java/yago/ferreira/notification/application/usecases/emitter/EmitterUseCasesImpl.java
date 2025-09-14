package yago.ferreira.notification.application.usecases.emitter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import yago.ferreira.notification.domain.model.SseEmitterModel;
import yago.ferreira.notification.domain.port.in.usecases.SseEmitterUseCase;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmitterUseCasesImpl implements SseEmitterUseCase {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter addEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter();
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError((e) -> {
            emitters.remove(userId);
            throw new RuntimeException("Erro ao abrir SseEmitter" + e.getMessage());
        });

        return emitter;
    }

    @Override
    public SseEmitterModel publishNotification(Long userId, SseEmitterModel sseEmitterModel) throws IOException {
        SseEmitter emitter = emitters.get(userId);

        if (emitter == null) {
            throw new RuntimeException("Emitter nao encontrado com o id " + userId);
        }

        try {
            emitter.send(SseEmitter.event().data(sseEmitterModel));
            return sseEmitterModel;
        } catch (IOException ioException) {
            throw new IOException("Error ao publicar pix ao SSE " + ioException.getMessage());
        }
    }
}
