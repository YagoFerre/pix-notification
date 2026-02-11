# PIX Notification System

Sistema de notificações em tempo real para eventos PIX, organizado em dois módulos Maven (multi‑module):

- notification-api: API REST responsável por criar usuários, persistir notificações e publicar eventos no RabbitMQ.
- notification: Worker que consome a fila RabbitMQ e entrega notificações para clientes via SSE (Server‑Sent Events).

Principais tecnologias usadas:
- Java 21, Spring Boot 3.x
- Apache Maven (multi‑module)
- PostgreSQL para persistência
- RabbitMQ para mensagens
- SSE para envio em tempo real
- Lombok, MapStruct, Jackson
- Docker / docker-compose para ambiente local

## Endpoints da API

Criar usuário
```cURL
POST /api/v1/usuario
```
```json
Request Body
{
    "nome": "João Silva",
    "email": "joao@example.com"
}
````

```json
Response
{
    "id": 1,
    "nome": "João Silva",
    "createdAt": "2024-...."
}
````

Enviar notificação
```cURL
POST /api/v1/notification
```
```json
Request Body
 {
    "message": "PIX recebido",
    "price": 9.99,
    "senderId": 1
}
````

```json
Response
{
    "message": "PIX recebido",
    "price": 9.99,
    "createdAt": "2024-....",
    "sender": {
        "id": 1,
        "nome": "João Silva",
        "createdAt": "2024-...."
    }
}
````

Conexão SSE para entrega de notificações
```cURL
GET /api/v1/emitter/{userId}
```
```json
Request Param
{userId}
````

```json
Response
200 OK
````

## Como rodar localmente

Pré-requisitos:
- Java 21 (OpenJDK/Corretto)
- Maven
- Docker e Docker Compose

## Iniciar infraestrutura local com Docker (banco e fila)
- Comando:
  - docker-compose up -d

Executar os serviços localmente
- Primeiro, rode a API (notification-api)
  - mvn -f pom.xml -pl notification-api -am spring-boot:run
- Em outra shell, rode o worker (notification)
  - mvn -f pom.xml -pl notification -am spring-boot:run


---

#### Durante o funcionamento, a API irá publicar a notificação na fila RabbitMQ e o worker irá entregar as notificações em tempo real para clientes conectados via SSE.

---


## Notas finais

- Este projeto é uma base simples e clara para um sistema de notificações em tempo real usando SSE, com persistência e fila de mensagens para desacoplamento.
- A arquitetura facilita a evolução para suportar múltiplos emissores e diferentes tipos de eventos no futuro.
