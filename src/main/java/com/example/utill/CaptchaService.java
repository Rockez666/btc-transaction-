package com.example.utill;

import jakarta.servlet.http.HttpSession;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

public class CaptchaService {
    public String generateCaptcha(HttpSession session) {
        String captcha = generateRandomCaptchaText();

        // Сохраняем CAPTCHA в сессию
        session.setAttribute("captcha", captcha);

        // Создаем изображение
        BufferedImage image = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 100, 40);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(captcha, 10, 30);

        // Преобразуем изображение в Base64 для отправки на фронт
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "PNG", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] imageBytes = baos.toByteArray();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }

    private String generateRandomCaptchaText() {
        Random random = new Random();
        StringBuilder captchaText = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            captchaText.append(random.nextInt(10)); // Генерация цифры
        }
        return captchaText.toString();
    }

    public boolean validateCaptcha(HttpSession session, String userInput) {
        // Получаем CAPTCHA из сессии
        String generatedCaptcha = (String) session.getAttribute("captcha");

        // Проверяем введенный пользователем текст
        return generatedCaptcha != null && generatedCaptcha.equals(userInput);
    }


}
