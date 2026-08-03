package org.example.kafka;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import org.example.dto.UserEvent;
import org.example.dto.UserOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(KafkaTestConfig.class)
class UserEventConsumerIntegrationTest {

    @RegisterExtension
    static GreenMailExtension greenMail =
            new GreenMailExtension(ServerSetupTest.SMTP);

    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

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

    @BeforeEach
    void setUp() {
        greenMail.reset();
    }

    @Test
    void createEvent_shouldSendWelcomeEmail() throws Exception {

        UserEvent event = new UserEvent(
                UserOperation.CREATE,
                "kafka-create@example.com"
        );

        kafkaTemplate.send("user-events", event).get();

        assertThat(greenMail.waitForIncomingEmail(10000, 1))
                .as("Письмо не было получено")
                .isTrue();

        MimeMessage[] messages = greenMail.getReceivedMessages();

        assertThat(messages) .hasSize(1);

        MimeMessage message = messages[0];

        assertEquals(
                "kafka-create@example.com",
                message.getRecipients(Message.RecipientType.TO)[0].toString()
        );

        assertThat(message.getRecipients(Message.RecipientType.TO)[0].toString())
                .isEqualTo("kafka-create@example.com");

        assertThat(message.getSubject())
                .isEqualTo("Добро пожаловать!");

        String content = message.getContent().toString();

        assertThat(content)
                .contains("Ваш аккаунт был успешно создан");
    }

    @Test
    void deleteEvent_shouldSendDeletedEmail() throws Exception {

        UserEvent event = new UserEvent(
                UserOperation.DELETE,
                "kafka-delete@example.com"
        );

        kafkaTemplate.send("user-events", event).get();

        assertThat(greenMail.waitForIncomingEmail(10000, 1))
                .as("Письмо не было получено")
                .isTrue();

        MimeMessage[] messages = greenMail.getReceivedMessages();

        assertThat(messages) .hasSize(1);

        MimeMessage message = messages[0];

        assertThat(message.getRecipients(Message.RecipientType.TO)[0].toString())
                .isEqualTo("kafka-delete@example.com");

        assertThat(message.getSubject())
                .isEqualTo("Пользователь удалён");

        String content = message.getContent().toString();

        assertThat(content)
                .contains("Ваш аккаунт был удалён");
    }
}
