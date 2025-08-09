package yago.ferreira.api.adapters.out.dto.event;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationCreatedEvent {
    private UsuarioEvent sender;
    private String message;
    private BigDecimal price;
    private LocalDateTime createdAt;
}
