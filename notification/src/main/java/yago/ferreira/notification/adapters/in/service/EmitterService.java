package yago.ferreira.notification.adapters.in.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import yago.ferreira.notification.adapters.out.dto.response.SseEmitterResponse;
import yago.ferreira.notification.domain.port.out.SseEmitterHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class EmitterService implements SseEmitterHandler {
    /*
     * Container que armazena as conexoes abertas
     */
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>(); // sessions: {"key": 2, "value: "emitter", "key": 34, "value: "emitter"}
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public SseEmitter openEmitterClient(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
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
    public void publishNotification(SseEmitterResponse sseEmitterResponse) {
        SseEmitter emitter = emitters.get(sseEmitterResponse.getId());

        if (emitter != null) {
            executor.execute(() -> {
                try {
                    emitter.send(SseEmitter.event().data(sseEmitterResponse));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }
}
