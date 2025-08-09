package yago.ferreira.api.adapters.out.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UsuarioResponse {
    private Long id;
    private String nome;
    private LocalDateTime createdAt;
}
