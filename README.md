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
Пример запроса:

json
{
  "email": "user@example.com",
  "operation": "CREATE"
}
🔄 Kafka
Топик: user-events

Формат сообщения:

json
{
  "operation": "CREATE|DELETE",
  "email": "user@example.com"
}
🐳 Запуск с Docker Compose
yaml
# Требуются зависимые сервисы:
# - Kafka (bootstrap-servers)
# - SMTP сервер (MailHog или другой)
Переменные окружения:

Переменная	Значение по умолчанию
SERVER_PORT	8082
SPRING_KAFKA_BOOTSTRAP_SERVERS	localhost:9092
SPRING_MAIL_HOST	mailhog
SPRING_MAIL_PORT	1025
