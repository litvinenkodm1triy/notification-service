package com.example.controller;

import com.example.dto.EmailRequest;
import com.example.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailRequest request) {
        System.out.println("REST request to send email to " + request.getEmail() + " with operation " + request.getOperation());
        emailService.sendNotificationEmail(request.getEmail(), request.getOperation());
        return ResponseEntity.accepted().build();
    }
}