package com.example.controller;

import com.example.dto.EmailRequest;
import com.example.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody EmailRequest request) {
        log.info("REST request to send email to {} with operation {}", request.getEmail(), request.getOperation());
        emailService.sendNotificationEmail(request.getEmail(), request.getOperation());
        return ResponseEntity.accepted().build();
    }
}