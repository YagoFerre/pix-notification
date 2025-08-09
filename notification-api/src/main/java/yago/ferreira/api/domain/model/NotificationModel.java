package yago.ferreira.api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class NotificationModel {
    private Long id;
    private UsuarioModel sender;
    private String message;
    private BigDecimal price;
    private LocalDateTime createdAt;

    public NotificationModel() {
    }

    public NotificationModel(Long id, UsuarioModel sender, String message, BigDecimal price, LocalDateTime createdAt) {
        this.id = id;
        this.sender = sender;
        this.message = message;
        this.price = price;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioModel getSender() {
        return sender;
    }

    public void setSender(UsuarioModel sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
