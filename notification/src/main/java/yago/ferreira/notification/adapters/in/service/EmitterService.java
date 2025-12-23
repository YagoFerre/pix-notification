package yago.ferreira.notification.adapters.in.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
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
    private static final Logger log = LoggerFactory.getLogger(EmitterService.class);
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
            // executa a tarefa de enviar uma notification em uma thread separada
            executor.execute(() -> {
                try {
                    emitter.send(SseEmitter.event().data(sseEmitterResponse));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }

    /*
     * Sends to user every 30 secs, and remove dead emitters
     */
    @Scheduled(fixedRate = 30000)
    public void sendHeartbeat() {
        if (emitters.isEmpty()) {
            return;
        }

        log.info("Sending heartbeat to {} active connections", emitters.size());

        emitters.forEach((userId, emitter) -> {
            // separate thread for execute this task
            executor.execute(() -> {
                try {
                    emitter.send(SseEmitter.event().comment("ping"));
                    log.info("💓 Heartbeat sent to userId: {}", userId);
                } catch (IOException e) {
                    log.warn("Heartbeat failed for userId: {}, connection is dead", userId);
                    emitters.remove(userId);
                    log.info("Successfully to remove userId: {} dead connection", userId);
                } catch (IllegalStateException e) {
                    log.warn("Heartbeat failed for userId: {}, emitter in illegal state", userId);
                    emitters.remove(userId);
                    log.info("Successfully to remove userId: {}", userId);
                }
            });
        });

    }


}
