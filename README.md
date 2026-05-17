Notification Service
Микросервис для отправки email-уведомлений о событиях пользователей (создание/удаление аккаунта).

🚀 Основные возможности
REST API — отправка уведомлений через HTTP-запрос

Kafka Consumer — асинхронная обработка событий пользователей

Email через SMTP — отправка писем с приветствием или уведомлением об удалении аккаунта

Swagger UI — документация API (доступна по /swagger-ui.html)

🛠 Технологии
Java 17

Spring Boot 3.4.0

Spring Kafka

Spring Mail

Testcontainers (Kafka + GreenMail)

Maven

📦 Эндпоинты
Метод	Путь	Описание
POST	/api/v1/notifications/send	Отправить email-уведомление
{
  "email": "user@example.com",
  "operation": "CREATE"
}
🔄 Kafka
Топик: user-events

Формат сообщения:

{
  "operation": "CREATE|DELETE",
  "email": "user@example.com"
}
  "operation": "CREATE|DELETE",
  "email": "user@example.com"
}
