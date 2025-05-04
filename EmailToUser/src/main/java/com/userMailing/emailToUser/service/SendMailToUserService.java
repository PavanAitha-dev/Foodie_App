package com.userMailing.emailToUser.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class SendMailToUserService {

    private final JavaMailSender javaMailSender;

    public SendMailToUserService(JavaMailSender javaMailSender) {

        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(String to, String body, String subject, boolean isHtml) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, isHtml); // true = send as HTML

            ClassPathResource resource = new ClassPathResource("static/img.png");helper.addInline("image", resource);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // handle exception appropriately
        }
    }
    public Double gstAmount(double amount){

        return amount*0.18+amount;
    }
}


