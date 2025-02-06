package com.example.service;

import com.example.entity.User;
import com.example.exception.EmailNotFoundException;
import com.example.exception.ThatUserIsVerifiedException;
import com.example.exception.VerificationCodeException;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void verifyEmail(String email, String verificationCode) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException("You could not be found, such email is not registered yet"));

        if (user.isVerified()) {
            throw new ThatUserIsVerifiedException("That user is already verified");
        } else if (user.getVerificationCode().equals(verificationCode)) {
            user.setVerified(true);
            user.setVerificationCode(null);
            userRepository.save(user);
        }
    }

    public void verifyResetPassword(String email, String code) {
        User userToReset = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("Email not found"));

        if (userToReset.getVerificationCode() == null || !userToReset.getVerificationCode().equals(code)) {
            throw new VerificationCodeException("The verification code is incorrect");
        } else {
            userToReset.setVerificationCode(null);
            userRepository.save(userToReset);
        }
        }
    }


