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
    private final Map<Long, Object> emittersLocks = new ConcurrentHashMap<>();

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public SseEmitter openEmitterClient(Long userId) {
        closeExistingConnections(userId);

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(userId, emitter);
        emittersLocks.put(userId, new Object());

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError((e) -> {
            emitters.remove(userId);
            log.error("Erro ao abrir SseEmitter {}", e.getMessage());
        });

        return emitter;
    }

    @Override
    public void publishNotification(SseEmitterResponse sseEmitterResponse) {
        SseEmitter emitter = emitters.get(sseEmitterResponse.getId());
        Object lock = emittersLocks.get(sseEmitterResponse.getId());

        if (lock == null) return;

        if (emitter != null) {
            // executa a tarefa de enviar uma notification em uma thread separada
            executor.execute(() -> {
                synchronized (lock) {
                    try {
                        emitter.send(SseEmitter.event().data(sseEmitterResponse));
                    } catch (IOException e) {
                        log.error("Error to publish message: {}", e.getMessage());
                        emitters.remove(sseEmitterResponse.getId());
                        emittersLocks.remove(sseEmitterResponse.getId());
                    }
                }
            });

        }
    }

    /*
     * The current user can not have twice connections at the same time
     * even in another window browser
     */
    private void closeExistingConnections(Long userId) {
        SseEmitter existingEmitter = emitters.get(userId);

        if (existingEmitter != null) {
            existingEmitter.complete(); // clean existing opened emitter user
            emitters.remove(userId);
            emittersLocks.remove(userId);

            log.warn("Successfully! to close exiting emitter for userId: {}", userId);
        }
    }


    /*
     * Sends to user every 10 secs, and remove dead emitters
     */
    @Scheduled(fixedRate = 10000)
    private void sendHeartbeat() {
        if (emitters.isEmpty()) {
            return;
        }

        log.info("Sending heartbeat to {} active connections", emitters.size());

        emitters.forEach((userId, emitter) -> {
            Object lock = emittersLocks.get(userId);
            if (lock == null) return;

            // separate thread for execute this task
            executor.execute(() -> {
                synchronized (lock) {
                    try {
                        emitter.send(SseEmitter.event().comment("ping"));
                        log.info("💓 Heartbeat sent to userId: {}", userId);
                    } catch (IOException e) {
                        log.error("Heartbeat failed for userId: {}, connection is dead", userId);
                        emitters.remove(userId);
                        emittersLocks.remove(userId);
                        log.error("Successfully to remove userId: {} dead connection", userId);
                    } catch (IllegalStateException e) {
                        log.error("Heartbeat failed for userId: {}, emitter in illegal state", userId);
                        emitters.remove(userId);
                        emittersLocks.remove(userId);
                        log.error("Successfully to remove userId: {}", userId);
                    }
                }
            });

        });

    }
}
