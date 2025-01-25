//package com.example.utill;
//
//import org.springframework.stereotype.Service;
//
//import java.security.SecureRandom;
//public class VerificationCodeGenerator {
//    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//    private static final int CODE_LENGTH = 6;
//    private static final SecureRandom random = new SecureRandom();
//
//    public static String generateVerificationCode() {
//        StringBuilder verificationCode = new StringBuilder(CODE_LENGTH);
//        for (int i = 0; i < CODE_LENGTH; i++) {
//            verificationCode.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
//        }
//        return verificationCode.toString();
//    }
//}
