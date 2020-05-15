package ru.itis.trello.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import ru.itis.trello.dto.MailDto;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service(value = "confirmationEmailService")
@EnableAsync
public class ConfirmationEmailServiceImpl implements EmailService {
    private Configuration configuration;
    private JavaMailSender emailSender;

    @Autowired
    public ConfirmationEmailServiceImpl(JavaMailSender emailSender, Configuration configuration) {
        this.emailSender = emailSender;
        this.configuration = configuration;
    }

    @Override
    @Async
    public void send(MailDto mailDto) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    false,
                    StandardCharsets.UTF_8.name());

            Template t = configuration.getTemplate(mailDto.getTemplate());
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, mailDto.getMap());

            helper.setTo(mailDto.getTo());
            helper.setText(html, true);
            helper.setSubject(mailDto.getSubject());

            emailSender.send(message);
        } catch (MailException | IOException | TemplateException | MessagingException e) {
            e.printStackTrace();
        }
    }
}