package com.example.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendNotificationEmail(String to, String operation) {
        System.out.println("=== ОТПРАВКА EMAIL ===");
        System.out.println("Кому: " + to);
        System.out.println("Операция: " + operation);

        if ("CREATE".equalsIgnoreCase(operation)) {
            System.out.println("Тема: Добро пожаловать!");
            System.out.println("Тело: Ваш аккаунт был успешно создан.");
        } else if ("DELETE".equalsIgnoreCase(operation)) {
            System.out.println("Тема: Аккаунт удалён");
            System.out.println("Тело: Ваш аккаунт был удалён.");
        } else {
            System.out.println("Неизвестная операция: " + operation);
            return;
        }

        System.out.println("=== КОНЕЦ ===");
        System.out.println();
    }
}