package yago.ferreira.api.adapters.in.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yago.ferreira.api.adapters.in.service.NotificationService;
import yago.ferreira.api.adapters.out.dto.request.NotificationRequest;
import yago.ferreira.api.adapters.out.dto.response.NotificationResponse;

@RestController
@RequestMapping("api/v1/notification")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(@RequestBody NotificationRequest notificationRequest, @RequestParam Long senderId) {
        NotificationResponse notificationSent = notificationService.sendNotification(notificationRequest, senderId);
        return ResponseEntity.ok(notificationSent);
    }
}
