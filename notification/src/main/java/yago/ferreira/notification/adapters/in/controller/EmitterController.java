package yago.ferreira.notification.adapters.in.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import yago.ferreira.notification.adapters.in.service.EmitterService;

@RestController
@RequestMapping("api/v1/emitter")
public class EmitterController {
    private final EmitterService emitterService;

    public EmitterController(EmitterService emitterService) {
        this.emitterService = emitterService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<SseEmitter> openServerSentEvents(@PathVariable Long userId) {
        return ResponseEntity.ok(emitterService.openEmitterClient(userId));
    }
}
