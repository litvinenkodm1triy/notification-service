
package com.example.controller;

import com.example.dto.EmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import jakarta.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class NotificationControllerIntegrationTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.0.1"));

    @Container
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> String.valueOf(greenMail.getSmtp().getPort()));
        registry.add("spring.mail.username", () -> "any");
        registry.add("spring.mail.password", () -> "any");
        registry.add("spring.mail.properties.mail.smtp.auth", () -> "false");
        registry.add("spring.mail.properties.mail.smtp.starttls.enable", () -> "false");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void sendEmail_WithValidRequest_ShouldReturnAcceptedAndSendEmail() throws Exception {
        EmailRequest request = new EmailRequest();
        request.setEmail("test@example.com");
        request.setOperation("CREATE");

        mockMvc.perform(post("/api/v1/notifications/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertThat(messages).hasSize(1);
        MimeMessage msg = messages[0];
        assertThat(msg.getSubject()).isEqualTo("Добро пожаловать!");
        assertThat(msg.getRecipients(jakarta.mail.Message.RecipientType.TO)[0].toString())
                .contains("test@example.com");
    }

    @Test
    void sendEmail_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
        EmailRequest request = new EmailRequest();
        request.setEmail("invalid-email");
        request.setOperation("CREATE");

        mockMvc.perform(post("/api/v1/notifications/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        assertThat(greenMail.getReceivedMessages()).isEmpty();
    }

    @Test
    void sendEmail_WithNullOperation_ShouldReturnBadRequest() throws Exception {
        EmailRequest request = new EmailRequest();
        request.setEmail("test@example.com");
        request.setOperation(null);

        mockMvc.perform(post("/api/v1/notifications/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        assertThat(greenMail.getReceivedMessages()).isEmpty();
    }
}