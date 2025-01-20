package com.example.service;

import com.example.entity.User;
import com.example.exception.VerificationCodeException;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationService {

    @Autowired
    private final UserRepository userRepository;

    public void verifyCode(String email, String code) {
        User user = userRepository.findByEmail(email);
        if (!user.getVerificationCode().equals(code)) {
            throw new VerificationCodeException("This code is not actual..");
        }
        user.setVerified(true);
    }


}
