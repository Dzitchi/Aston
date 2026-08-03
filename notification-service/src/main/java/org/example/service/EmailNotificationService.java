package org.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    private static final Logger log =
            LoggerFactory.getLogger(EmailNotificationService.class);

    private final JavaMailSender mailSender;

    public EmailNotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendUserCreatedEmail(String email) {

        log.info("Отправка письма пользователю: {}", email);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Добро пожаловать!");
        message.setText(
                "Здравствуйте!\n\n" +
                        "Ваш аккаунт был успешно создан."
        );

        try {
            mailSender.send(message);

            log.info(
                    "Письмо успешно отправлено на {}",
                    email
            );
        } catch (Exception e) {
            log.error(
                    "Ошибка отправки письма на {}",
                    email,
                    e
            );

            throw e;
        }
    }

    public void sendUserDeletedEmail(String email) {

        log.info("Отправка уведомления об удалении пользователю: {}", email);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Пользователь удалён");
        message.setText(
                "Здравствуйте!\n\n" +
                        "Ваш аккаунт был удалён."
        );

        try {
            mailSender.send(message);

            log.info(
                    "Уведомление об удалении отправлено на {}",
                    email
            );
        } catch (Exception e) {
            log.error(
                    "Ошибка отправки уведомления об удалении на {}",
                    email,
                    e
            );

            throw e;
        }
    }
}
