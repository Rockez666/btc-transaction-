package com.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;


    @Async
    public void sendVerificationEmail(String email, String verificationCode) {
        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);

        try {
            String htmlContent = templateEngine.process("emailTemplate", context);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Verification Code");
            mimeMessageHelper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

