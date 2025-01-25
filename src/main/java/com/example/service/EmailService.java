package com.example.service;

import com.example.utill.JWTUtill;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final JWTUtill jwtUtill;

    @Async
    public void sendVerificationEmail(String email ) {
        String token = jwtUtill.generateEmailVerifyToken(email);



        String verificationLink = "http://localhost:5173/verification?token=" +
                URLEncoder.encode(token, StandardCharsets.UTF_8);

        Context context = new Context();
        context.setVariable("verificationLink", verificationLink);
        context.setVariable("email", email);

        try {
            String htmlContent = templateEngine.process("emailTemplate", context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Verification Link");
            mimeMessageHelper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}