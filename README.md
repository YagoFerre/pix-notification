# notification (pix-notification)

Projeto multi-módulo em Java (Spring Boot) para envio e entrega de notificações em tempo real.
É composto por dois módulos principais:

- `notification-api` — API que persiste notificações e publica eventos (RabbitMQ).
- `notification` — Serviço que consome a fila do RabbitMQ e entrega notificações via Server-Sent Events (SSE) para clientes conectados.

Este README descreve arquitetura, como executar localmente (com Docker Compose para dependências), endpoints, formatos de payload e exemplos de uso.

Descrição
O sistema permite:
- Criar usuários (API).
- Criar notificações (API) associados a usuários.
- Publicar eventos de notificação em uma fila RabbitMQ.
- Serviço consumidor lê a fila e encaminha em tempo real via SSE para clientes abertos pelo usuário destinatário.

Arquitetura
- notification-api (Spring Boot)
  - Persistência via JPA/Hibernate (PostgreSQL).
  - Publicação de evento para RabbitMQ (Jackson converter).
- notification (Spring Boot)
  - Escuta fila RabbitMQ (`notification.v1.sent-notification`).
  - Mantém SseEmitter por usuário (mapa ConcurrentHashMap).
  - Envia evento SSE ao usuário conectado.

Dependências externas (rodar via docker-compose incluido)
- PostgreSQL 15.3
- RabbitMQ 3.11 (management plugin disponível em 15672)



Endpoints (resumo)

notification-api (HTTP)
- POST /api/v1/usuario
  - Cria usuário.
  - Request body: UsuarioRequest
- POST /api/v1/notification
  - Salva notificação e publica evento para fila RabbitMQ.
  - Request body: NotificationRequest

notification (HTTP)
- GET /api/v1/emitter/{userId
}
  - Abre conexão SSE para receber notificações destinadas ao userId.
  - Retorna `SseEmitter` e envia eventos quando mensagens chegam pela fila.

Modelos (JSON)
- NotificationRequest (enviado para /api/v1/notification)
```json
{
    "message": "Olá, você recebeu uma cobrança",
    "price": 34.50,
    "senderId": 1
}
```

- UsuarioRequest (enviado para /api/v1/usuario)
```json
{
    "nome": "Yago Ferreira",
    "email": "yago@example.com"
}
```

- NotificationResponse (ex.: retornado pela API e via fila)
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

- SseEmitterResponse (payload SSE enviado pelo serviço de notificação)
```json
{
    "id": 123, // id da notificação
    "data": { /* NotificationResponse */},
    "event": "notification.created"
}
```

Exemplo de uso (fluxo típico)

1. Criar usuário
```bash
curl -X POST http: //localhost:8080/api/v1/usuario \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Yago",
    "email": "yago@example.com"
}'
```
Resposta: UsuarioResponse com id.

2. Abrir SSE no cliente (no browser / app) para receber notificações do userId = 1
Exemplo JavaScript:
```js
const userId = 1;
const es = new EventSource(`http: //localhost:8090/api/v1/emitter/${userId}`);
es.onmessage = function(e) {
  console.log("Evento SSE recebido:", e.data);
};
es.addEventListener("notification.created", function(e) {
  console.log("Evento específico:", e.data);
});
es.onerror = function(err) {
  console.error("SSE error", err);
};
```
