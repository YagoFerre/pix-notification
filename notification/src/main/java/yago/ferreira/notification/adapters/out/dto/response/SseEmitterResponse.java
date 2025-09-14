package yago.ferreira.notification.adapters.out.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SseEmitterResponse {
    private Long id;
    private NotificationResponse data;
    private String event;
}
