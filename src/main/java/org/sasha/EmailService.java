package org.sasha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        logger.info("Подготовка к отправке email. Адрес: {}, Тема: {}", to, subject);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("hertzxq@mail.ru");

            mailSender.send(message);
            logger.info("Email успешно отправлен на адрес: {}", to);
        } catch (Exception e) {
            logger.error("Ошибка при отправке email: {}", e.getMessage(), e);
            throw e;
        }
    }
}
