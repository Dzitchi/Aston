package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.EmailNotificationRequest;
import org.example.service.EmailNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class EmailNotificationController {

    private static final Logger log =
            LoggerFactory.getLogger(EmailNotificationController.class);

    private final EmailNotificationService emailNotificationService;

    public EmailNotificationController(
            EmailNotificationService emailNotificationService
    ) {
        this.emailNotificationService = emailNotificationService;
    }

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(
            @Valid @RequestBody EmailNotificationRequest request
    ) {

        log.info(
                "Запрос на отправку email: operation={}, email={}",
                request.getOperation(),
                request.getEmail()
        );

        switch (request.getOperation().toUpperCase()) {

            case "CREATE" ->
                    emailNotificationService.sendUserCreatedEmail(
                            request.getEmail()
                    );

            case "DELETE" ->
                    emailNotificationService.sendUserDeletedEmail(
                            request.getEmail()
                    );

            default -> {
                log.warn(
                        "Неизвестная операция: {}",
                        request.getOperation()
                );

                return ResponseEntity.badRequest()
                        .body("Неизвестная операция: " + request.getOperation());
            }
        }

        return ResponseEntity.ok("Email успешно отправлен");
    }
}
