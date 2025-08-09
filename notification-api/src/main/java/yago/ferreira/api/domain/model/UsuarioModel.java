package yago.ferreira.api.domain.model;

import java.time.LocalDateTime;

public class UsuarioModel {
    private Long id;
    private String nome;
    private String email;
    private LocalDateTime createdAt;

    public UsuarioModel() {
    }

    public UsuarioModel(Long id, String nome, String email, LocalDateTime createdAt) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
