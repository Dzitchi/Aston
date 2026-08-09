package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.EmailNotificationRequest;
import org.example.service.EmailNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@Tag(
        name = "Notifications",
        description = "API для отправки email-уведомлений"
)
public class EmailNotificationController {

    private static final Logger log =
            LoggerFactory.getLogger(EmailNotificationController.class);

    private final EmailNotificationService emailNotificationService;

    public EmailNotificationController(
            EmailNotificationService emailNotificationService
    ) {
        this.emailNotificationService = emailNotificationService;
    }

    @Operation(
            summary = "Отправить email",
            description =
                    "Отправляет email-уведомление. " +
                            "Поддерживаются операции CREATE и DELETE."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Email успешно отправлен"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные или неизвестная операция"
            )
    })
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
