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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final JWTUtill jwtUtill;

    private static final int TOKEN_LENGTH = 32;
    private static final SecureRandom random = new SecureRandom();


    @Async
    public void sendVerificationEmail(String email, String verificationCode) {

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

    public void sendLinkToResetPassword(String email, String verificationCode) {
        String linkToResetPassword = "http://localhost:5173/login?email=" +
                URLEncoder.encode(email, StandardCharsets.UTF_8) +
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

    public void sendSuccessMessageAboutChangePassword(String email) {
        Context context = new Context();
        context.setVariable("email", email);

        try {
            String htmlContent = templateEngine.process("passwordResetSuccessMessage", context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Success Reset Password");
            mimeMessageHelper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String generateCode() {
        byte[] randomBytes = new byte[TOKEN_LENGTH];
        random.nextBytes(randomBytes);
        String token = Base64.getEncoder().encodeToString(randomBytes);
        return hashToken(token);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(token.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Code hashing error", e);
        }


    }
}