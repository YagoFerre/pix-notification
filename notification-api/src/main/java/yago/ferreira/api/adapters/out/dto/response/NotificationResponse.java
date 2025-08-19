package yago.ferreira.api.adapters.out.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationResponse {
    private UsuarioResponse sender;
    private String message;
    private BigDecimal price;
    private LocalDateTime createdAt;
}
