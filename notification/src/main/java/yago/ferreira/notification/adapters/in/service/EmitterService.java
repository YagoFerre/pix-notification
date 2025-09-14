package yago.ferreira.notification.adapters.in.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import yago.ferreira.notification.adapters.out.dto.response.SseEmitterResponse;
import yago.ferreira.notification.domain.port.out.SseEmitterHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmitterService implements SseEmitterHandler {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter openEmitterClient(Long userId) {
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
    public SseEmitterResponse publishNotification(Long userId, SseEmitterResponse sseEmitterResponse) throws IOException {
        SseEmitter emitter = emitters.get(userId);

        if (emitter == null) {
            throw new RuntimeException("Emitter nao encontrado com o id " + userId);
        }

        try {
            emitter.send(SseEmitter.event().data(sseEmitterResponse));
            return sseEmitterResponse;
        } catch (IOException ioException) {
            throw new IOException("Error ao publicar pix ao SSE " + ioException.getMessage());
        }
    }
}
