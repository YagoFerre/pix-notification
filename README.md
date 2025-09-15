# 🚀 PIX Notification System

> Sistema de notificações em tempo real para transações PIX utilizando Spring Boot e RabbitMQ

## ✨ Funcionalidades

- 📱 Notificações em tempo real
- 💳 Rastreamento de transações PIX
- 👥 Gerenciamento de usuários
- 🔄 Integração simplificada

## 🏗️ Arquitetura

O projeto é dividido em dois módulos principais:

### 📬 notification-api
- API REST para gestão de notificações e usuários
- Integração com PostgreSQL
- Publicação de eventos no RabbitMQ

### 📲 notification
- Serviço de consumo de eventos
- Entrega de notificações via Server-Sent Events (SSE)
- Processamento em tempo real

## 🛠️ Tecnologias

- ☕ Java + Spring Boot
- 🐘 PostgreSQL 15.3
- 🐰 RabbitMQ 3.11
- 📡 Server-Sent Events (SSE)

## 🚀 Como Usar

### 1. Iniciar as Dependências
```bash
docker-compose up -d
```

### 2. Criar um Usuário
```bash
curl -X POST http://localhost:8080/api/v1/usuario \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Yago",
    "email": "yago@example.com"
  }'
```

### 3. Enviar uma Notificação
```bash
curl -X POST http://localhost:8080/api/v1/notification \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Você recebeu um Pix",
    "price": 50.0,
    "senderId": 1
  }'
```

## 📡 Endpoints

### API REST (notification-api)
- `POST /api/v1/usuario` - Cadastro de usuários
- `POST /api/v1/notification` - Envio de notificações

### SSE (notification)
- `GET /api/v1/emitter/{userId}` - Stream de notificações em tempo real

## 🔍 Exemplos de Payload

### Envio de Notificação
```json
{
  "message": "Olá, você recebeu uma cobrança",
  "price": 34.50,
  "senderId": 1
}
```

### Resposta da Notificação
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

## 📝 Licença

Este projeto está sob a licença MIT.

---

Feito por [Yago Ferreira](https://github.com/YagoFerre)
