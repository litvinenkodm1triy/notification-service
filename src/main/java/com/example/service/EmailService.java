package com.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendNotificationEmail(String to, String operation) {
        String subject;
        String body;

        if ("CREATE".equalsIgnoreCase(operation)) {
            subject = "Добро пожаловать!";
            body = "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
        } else if ("DELETE".equalsIgnoreCase(operation)) {
            subject = "Аккаунт удалён";
            body = "Здравствуйте! Ваш аккаунт был удалён.";
        } else {
            log.warn("Неизвестная операция: {}, письмо не отправлено", operation);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
            log.info("Email отправлен на {} с темой '{}'", to, subject);
        } catch (Exception e) {
            log.error("Ошибка отправки email на {}: {}", to, e.getMessage());
            throw new RuntimeException("Не удалось отправить письмо", e);
        }
    }
}