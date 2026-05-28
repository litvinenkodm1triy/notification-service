package com.example.service;

import com.example.dto.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void consumeUserEvent(UserEvent event) {
        log.info("Получено событие из Kafka: {}", event);
        try {
            emailService.sendNotificationEmail(event.getEmail(), event.getOperation());
            log.info("Уведомление обработано для {}", event.getEmail());
        } catch (Exception e) {
            log.error("Не удалось отправить email для события {}: {}", event, e.getMessage());
        }
    }
}