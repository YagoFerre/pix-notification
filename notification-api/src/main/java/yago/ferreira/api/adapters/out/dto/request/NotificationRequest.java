package yago.ferreira.api.adapters.out.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class NotificationRequest {
    private UsuarioRequest sender;
    private String message;
    private BigDecimal price;
}
