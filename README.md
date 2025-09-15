# notification (pix-notification)

Projeto multi-módulo em Java (Spring Boot) para envio e entrega de notificações em tempo real.
É composto por dois módulos principais:

- `notification-api` — API que persiste notificações e publica eventos (RabbitMQ).
- `notification` — Serviço que consome a fila do RabbitMQ e entrega notificações via Server-Sent Events (SSE) para clientes conectados.

Este README descreve arquitetura, como executar localmente (com Docker Compose para dependências), endpoints, formatos de payload e exemplos de uso.

Sumário
- Descrição
- Arquitetura
- Requisitos
- Variáveis/Configurações importantes
- Como executar (Docker + aplicações)
- Endpoints e payloads
- Exemplo de uso (curl + cliente SSE em JavaScript)
- Diagnóstico / Problemas comuns
- Contribuição / Licença

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

Requisitos
- Java 21 (Corretto/Adopt/OpenJDK)
- Maven 3.6+ (ou usar wrapper se existir)
- Docker + Docker Compose (para facilitar execução de infra local)
- IDE: IntelliJ (configurações de annotation processing e Lombok/MapStruct já presentes nos arquivos de projeto)

Configurações importantes
- Oracle: JDK 21 compatibility (projeto configurado para Java 21)
- Filas:
  - Nome da fila: `notification.v1.sent-notification`
- Docker Compose (arquivo incluso) expõe:
  - PostgreSQL: 5432, credenciais no docker-compose (DB `notificationdb`, user `postgres`, pass `postgres`)
  - RabbitMQ: 5672 (amqp), 15672 (management UI) — usuário guest/guest

Exemplo docker-compose (já incluído no projeto)
- Para inicializar banco e rabbit:
  docker-compose.yml (fornecido) cria `db_pix_notification` e `mq_pix_notification`.

Como executar (localmente)

1) Iniciar dependências (Postgres + RabbitMQ)
```bash
# na raiz do projeto (onde docker-compose.yml está)
docker-compose up -d
# verificar status
docker-compose ps
# acessar rabbitmq management: http://localhost:15672 (guest/guest)
```

2) Build do projeto (opcional)
Na raiz (agregador pom `pix-notification`):
```bash
mvn clean package -DskipTests
```

3a) Executar módulos via Maven (em processos separados)
```bash
# Iniciar API (module notification-api)
mvn -pl notification-api spring-boot:run

# Em outro terminal iniciar Serviço de Notificação (module notification)
mvn -pl notification spring-boot:run
```

3b) Ou executar jars gerados (após package)
```bash
# Exemplo
java -jar notification-api/target/notification-api-0.0.1-SNAPSHOT.jar
java -jar notification/target/notification-0.0.1-SNAPSHOT.jar
```

Propriedades Spring (exemplos — defina em application.yml/properties ou env vars)
- PostgreSQL (notification-api)
  spring.datasource.url=jdbc:postgresql://localhost:5432/notificationdb
  spring.datasource.username=postgres
  spring.datasource.password=postgres
- RabbitMQ (ambos os módulos que usam)
  spring.rabbitmq.host=localhost
  spring.rabbitmq.port=5672
  spring.rabbitmq.username=guest
  spring.rabbitmq.password=guest

Endpoints (resumo)

notification-api (HTTP)
- POST /api/v1/usuario
  - Cria usuário.
  - Request body: UsuarioRequest
- POST /api/v1/notification
  - Salva notificação e publica evento para fila RabbitMQ.
  - Request body: NotificationRequest

notification (HTTP)
- GET /api/v1/emitter/{userId}
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
  "id": 123,              // id da notificação
  "data": { /* NotificationResponse */ },
  "event": "notification.created"
}
```

Exemplo de uso (fluxo típico)

1. Criar usuário
```bash
curl -X POST http://localhost:8080/api/v1/usuario \
  -H "Content-Type: application/json" \
  -d '{"nome":"Yago","email":"yago@example.com"}'
```
Resposta: UsuarioResponse com id.

2. Abrir SSE no cliente (no browser / app) para receber notificações do userId = 1
Exemplo JavaScript:
```js
const userId = 1;
const es = new EventSource(`http://localhost:8090/api/v1/emitter/${userId}`);
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
Observação: os serviços podem rodar em portas diferentes; por padrão `notification-api` fica na 8080 e `notification` na 8090 — ajuste conforme sua configuração.

3. Publicar notificação via API
```bash
curl -X POST http://localhost:8080/api/v1/notification \
  -H "Content-Type: application/json" \
  -d '{"message":"Teste SSE","price":10.0,"senderId":1}'
```
- A API salva a notificação no Postgres e publica um evento para a fila `notification.v1.sent-notification`.
- O serviço `notification` consome a fila e, se houver um SseEmitter para o user correspondente, envia o SseEmitterResponse via SSE.

Comportamento SSE no backend
- Emitters são guardados em um Map<Long, SseEmitter>.
- Ao conectar o emitter, o serviço registra callbacks onCompletion, onTimeout e onError para remover o emitter quando necessário.

Fila RabbitMQ
- Nome da fila: `notification.v1.sent-notification`
- Para inspecionar: abra http://localhost:15672 (guest/guest) e verifique filas / mensagens.

Debug e problemas comuns
- Conexão recusada ao Postgres
  - Verifique docker-compose up, portas (5432) e credenciais.
  - Confirme URL em application.properties.
- Conexão recusada ao RabbitMQ
  - Verifique docker-compose up, portas (5672), usuário/senha (guest/guest para local).
  - Verifique logs do RabbitMQ e do app.
- SSE não recebe mensagens
  - Verifique se o emitter foi criado (cliente abriu `/api/v1/emitter/{userId}`).
  - Verifique se o consumidor Rabbit está ativo e recebendo mensagens (logs).
  - Cheque se o campo userId usado ao abrir SSE coincide com o sender/receiver do evento (mapeamento de ids).
- MapStruct / Lombok
  - Se IDE não gerar classes, habilite annotation processing (IntelliJ: Settings → Build, Execution, Deployment → Compiler → Annotation Processors).
- CORS
  - Se cliente browser e backend em domínios/portas diferentes, configure CORS para permitir EventSource.

Testes
- Existem testes de contexto Spring Boot (ex.: NotificationApplicationTests). Rode:
```bash
mvn test
```

Como contribuir
- Abra issues para bugs / melhorias.
- Fork → branch com feature → PR com descrição e testes.
- Mantenha compatibilidade com Java 21 e Spring Boot 3.5.x.

Estrutura do repositório (resumo)
- pom.xml (agregador `pix-notification`)
- notification-api (módulo)
  - controllers, services, adapters, entities, repositories
- notification (módulo)
  - controllers, services, listeners (RabbitMQ), SSE handler
- docker-compose.yml (postgres + rabbitmq)
- HELP.md (links e docs úteis)

Links úteis
- RabbitMQ management: http://localhost:15672 (guest/guest)
- Spring Boot docs: https://spring.io/projects/spring-boot
- RabbitMQ + Spring AMQP: https://docs.spring.io/spring-amqp/docs/current/reference/html/

Licença
- (inserir licença do projeto aqui — ex: MIT/Apache-2.0)

Contato
- Mantido por: yago.ferreira (informações de contato / repo)

Se quiser, posso:
- Gerar exemplos de application.yml para ambos módulos.
- Sugerir scripts Dockerfile para empacotar serviços como containers.
- Ajudar a ajustar CORS/ports e configuração de portas padrão.