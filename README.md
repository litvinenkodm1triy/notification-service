# Notification Service

Микросервис для отправки email-уведомлений.  
Слушает события из Kafka (топик `user-events`) и отправляет письма при создании или удалении пользователя.  
Также предоставляет отдельный REST API для отправки уведомлений напрямую (минуя Kafka).

## Технологии

- Java 17, Spring Boot 3.4.0
- Spring Kafka (consumer)
- Spring Mail (SMTP)
- Lombok, Jakarta Validation
- Testcontainers + GreenMail (тесты)

## Основной функционал

| Компонент | Описание |
|-----------|----------|
| **Kafka Consumer** (`UserEventConsumer`) | Получает события из топика `user-events` с полями `operation` (CREATE/DELETE) и `email`. Вызывает `EmailService`. |
| **EmailService** | Формирует текст письма в зависимости от операции и отправляет через SMTP. |
| **REST API** (`POST /api/v1/notifications/send`) | Позволяет отправить письмо синхронно (дублирует логику consumer). |

## REST API

**Endpoint:** `POST /api/v1/notifications/send`

**Тело запроса (JSON):**
```json
{
  "email": "user@example.com",
  "operation": "CREATE"
}
