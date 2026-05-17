package com.example.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void shouldSendEmailForCreateOperation() {
        String to = "user@example.com";
        String operation = "CREATE";

        emailService.sendNotificationEmail(to, operation);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage message = captor.getValue();
        assertThat(message.getTo()).containsExactly(to);
        assertThat(message.getSubject()).isEqualTo("Добро пожаловать!");
        assertThat(message.getText()).contains("аккаунт на сайте был успешно создан");
    }

    @Test
    void shouldSendEmailForDeleteOperation() {
        String to = "user@example.com";
        String operation = "DELETE";

        emailService.sendNotificationEmail(to, operation);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage message = captor.getValue();
        assertThat(message.getTo()).containsExactly(to);
        assertThat(message.getSubject()).isEqualTo("Аккаунт удалён");
        assertThat(message.getText()).contains("аккаунт был удалён");
    }

    @Test
    void shouldNotSendEmailForUnknownOperation() {
        String to = "user@example.com";
        String operation = "UNKNOWN";

        emailService.sendNotificationEmail(to, operation);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }
}