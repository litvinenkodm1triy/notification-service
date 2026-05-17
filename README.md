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
operation: CREATE или DELETE (регистр не важен)

email: должен быть валидным

Ответ: 202 Accepted

Интеграция с user-service
user-service при создании/удалении пользователя отправляет событие в Kafka.

notification-service получает событие и отправляет email на адрес пользователя.

Используется общий топик user-events.

Тесты
EmailServiceTest – unit‑тесты с моком JavaMailSender.

NotificationControllerIntegrationTest – интеграционный тест REST API (Testcontainers Kafka + GreenMail).

UserEventConsumerIntegrationTest – интеграционный тест consumer (отправка события в Kafka → получение → отправка письма).

Все интеграционные тесты используют Testcontainers 1.21.4 (совместимо с user-service) и GreenMail для эмуляции SMTP.

Конфигурация (основные параметры)
Свойство	Переменная окружения	Значение по умолчанию
spring.kafka.bootstrap-servers	KAFKA_BOOTSTRAP_SERVERS	localhost:9092
spring.mail.host	SPRING_MAIL_HOST	localhost
spring.mail.port	SPRING_MAIL_PORT	1025
Тексты писем
CREATE: «Здравствуйте! Ваш аккаунт на сайте был успешно создан.»

DELETE: «Здравствуйте! Ваш аккаунт был удалён.»

Требования
Java 17

Kafka (можно поднять локально через Docker)

SMTP-сервер (для разработки рекомендуется MailHog)

Структура проекта
text
src/
├── main/java/com/example/
│   ├── controller/NotificationController
│   ├── service/EmailService, UserEventConsumer
│   ├── dto/EmailRequest, UserEvent
│   └── DemoApplication
├── main/resources/application.properties
└── test/java/com/example/
    ├── controller/NotificationControllerIntegrationTest
    ├── consumer/UserEventConsumerIntegrationTest
    ├── service/EmailServiceTest
    └── DemoApplicationTests
Примечания
Сервис не зависит от базы данных.

Для локальной разработки можно использовать только REST API, не запуская Kafka.

В production необходимо настроить реальный SMTP (Gmail, Яндекс и т.д.) с аутентификацией и TLS.
