package com.example.service;

import com.example.dto.UserEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventConsumer {

    private final EmailService emailService;

    public UserEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void consumeUserEvent(UserEvent event) {
        System.out.println("Получено событие из Kafka: " + event);
        try {
            emailService.sendNotificationEmail(event.getEmail(), event.getOperation());
            System.out.println("Уведомление обработано для " + event.getEmail());
        } catch (Exception e) {
            System.err.println("Не удалось отправить email: " + e.getMessage());
        }
    }
}