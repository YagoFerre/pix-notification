# pix-notification ⚡️

Um projeto multi-módulo em Java (Spring Boot) pra enviar notificações em tempo real via RabbitMQ + Server-Sent Events (SSE). Leve, simples e fácil de integrar.

Principais módulos
- `notification-api` — API REST: persiste notificações/usuários e publica eventos no RabbitMQ.
- `notification` — Serviço consumidor: lê a fila e empurra notificações em tempo real via SSE para clientes conectados.

O que ele faz
- Criar usuários
- Criar notificações associadas a usuários
- Publicar eventos em uma fila RabbitMQ
- Consumir a fila e entregar notificações em tempo real com SSE

Arquitetura - Ports and Adapters Hexagonal
- notification-api
  - Spring Boot + JPA/Hibernate (Postgres)
  - Publica eventos para RabbitMQ (JSON via Jackson)
- notification
  - Spring Boot que escuta a fila `notification.v1.sent-notification`
  - Envia SSE para o usuário quando a mensagem chega

Dependências (recomendado via docker-compose)
- PostgreSQL 15.3
- RabbitMQ 3.11 (+ management em 15672)

Endpoints principais

notification-api
- POST /api/v1/usuario
  - Cria usuário
  - Body: UsuarioRequest

- POST /api/v1/notification
  - Salva notificação e publica evento na fila
  - Body: NotificationRequest

notification (SSE)
- GET /api/v1/emitter/{userId}
  - Abre conexão SSE para receber notificações do userId
  - Retorna SseEmitter; quando chegar mensagem na fila, o serviço envia o evento

Modelos (exemplos JSON)

NotificationRequest (POST /api/v1/notification)
```json
{
  "message": "Olá, você recebeu uma cobrança",
  "price": 34.50,
  "senderId": 1
}
```

UsuarioRequest (POST /api/v1/usuario)
```json
{
  "nome": "Yago Ferreira",
  "email": "yago@example.com"
}
```

NotificationResponse (ex.: retornado pela API / enviado na fila)
```json
{
  "message": "Olá, você recebeu uma cobrança",
  "price": 34.50,
  "createdAt": "2025-09-15T12:34:56",
  "sender": {
    "id": 1,
    "nome": "Yago Ferreira",
    "createdAt": "2025-09-15T12:30:00"
  }
}
```

SseEmitterResponse (payload SSE)
```json
{
  "id": 123,
  "data": { /* NotificationResponse */ },
  "event": "notification.created"
}
```

Exemplo de uso (fluxo)
1) Criar usuário:
```bash
curl -X POST http://localhost:8080/api/v1/usuario \
  -H "Content-Type: application/json" \
  -d '{"nome":"Yago","email":"yago@example.com"}'
```

3) Criar notificação (API)
```bash
curl -X POST http://localhost:8080/api/v1/notification \
  -H "Content-Type: application/json" \
  -d '{"message":"Você recebeu um Pix","price":50.0,"senderId":1}'
```
