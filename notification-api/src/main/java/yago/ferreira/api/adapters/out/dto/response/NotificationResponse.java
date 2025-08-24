package yago.ferreira.api.adapters.out.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private String message;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private UsuarioResponse sender;
}
