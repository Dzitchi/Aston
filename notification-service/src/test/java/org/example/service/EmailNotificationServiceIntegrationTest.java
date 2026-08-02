package org.example.service;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class EmailNotificationServiceIntegrationTest {

    @RegisterExtension
    static GreenMailExtension greenMail =
            new GreenMailExtension(ServerSetupTest.SMTP)
                    .withConfiguration(
                            GreenMailConfiguration.aConfig()
                                    .withDisabledAuthentication()
                    );

    @DynamicPropertySource
    static void mailProperties(DynamicPropertyRegistry registry) {

        registry.add(
                "spring.mail.host",
                () -> "localhost"
        );

        registry.add(
                "spring.mail.port",
                () -> ServerSetupTest.SMTP.getPort()
        );

        registry.add(
                "spring.mail.properties.mail.smtp.auth",
                () -> "false"
        );

        registry.add(
                "spring.mail.properties.mail.smtp.starttls.enable",
                () -> "false"
        );
    }

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Test
    void sendUserCreatedEmail_shouldSendEmail() throws Exception {

        String recipient = "recipient@example.com";

        emailNotificationService.sendUserCreatedEmail(recipient);

        assertTrue(
                greenMail.waitForIncomingEmail(5000, 1),
                "Письмо не было получено"
        );

        MimeMessage[] messages = greenMail.getReceivedMessages();

        assertEquals(1, messages.length);

        MimeMessage message = messages[0];

        assertEquals(
                recipient,
                message.getRecipients(Message.RecipientType.TO)[0]
                        .toString()
        );

        assertEquals(
                "Добро пожаловать!",
                message.getSubject()
        );

        String content = message.getContent().toString();

        assertTrue(
                content.contains("Ваш аккаунт был успешно создан")
        );
    }

    @Test
    void sendUserDeletedEmail_shouldSendEmail() throws Exception {

        String recipient = "deleted@example.com";

        emailNotificationService.sendUserDeletedEmail(recipient);

        assertTrue(
                greenMail.waitForIncomingEmail(5000, 1),
                "Письмо не было получено"
        );

        MimeMessage[] messages = greenMail.getReceivedMessages();

        assertEquals(1, messages.length);

        MimeMessage message = messages[0];

        assertEquals(
                recipient,
                message.getRecipients(Message.RecipientType.TO)[0]
                        .toString()
        );

        assertEquals(
                "Пользователь удалён",
                message.getSubject()
        );

        String content = message.getContent().toString();

        assertTrue(
                content.contains("Ваш аккаунт был удалён")
        );
    }
}
