package org.example.kafka;

import org.example.dto.UserEvent;
import org.example.service.EmailNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(UserEventConsumer.class);

    private final EmailNotificationService emailNotificationService;

    public UserEventConsumer(EmailNotificationService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;

        System.out.println("!!! UserEventConsumer СОЗДАН !!!");
    }

    @KafkaListener(topics = "user-events")
    public void handleUserEvent(UserEvent event) {

        log.info(
                "Получено событие Kafka: operation={}, email={}",
                event.getOperation(),
                event.getEmail()
        );

        if ("CREATE".equals(event.getOperation())) {

            emailNotificationService.sendUserCreatedEmail(
                    event.getEmail()
            );

        } else if ("DELETE".equals(event.getOperation())) {

            emailNotificationService.sendUserDeletedEmail(
                    event.getEmail()
            );

        } else {

            log.warn(
                    "Неизвестная операция: {}",
                    event.getOperation()
            );
        }
    }
}
