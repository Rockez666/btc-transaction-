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
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final JWTUtill jwtUtill;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();


    @Async
    public void sendVerificationEmail(String email,String verificationCode) {

        String verificationLink = "http://localhost:5173/verification?" +
                "email=" + URLEncoder.encode(email, StandardCharsets.UTF_8) +
                "&code=" + URLEncoder.encode(verificationCode, StandardCharsets.UTF_8);

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

    public void sendVerificationLinkToResetPassword(String email,String verificationCode) {
        // TODO: need to change the link
        String linkToResetPassword = "http://localhost:5173/verification?" +
                "email=" + URLEncoder.encode(email, StandardCharsets.UTF_8) +
                "&code=" + URLEncoder.encode(verificationCode, StandardCharsets.UTF_8);

        Context context = new Context();
        context.setVariable("linkToResetPassword", linkToResetPassword);
        context.setVariable("email", email);

        try {
            String htmlContent = templateEngine.process("passwordReset", context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Reset Password");
            mimeMessageHelper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

   public String generateCode() {
       StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
          int randomIndex = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(randomIndex));
       }
        return code.toString();}
}