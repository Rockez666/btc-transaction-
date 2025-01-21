package com.example.service;

import com.example.command.VerifyEmailCommand;
import com.example.entity.User;
import com.example.exception.UserNotFoundException;
import com.example.exception.VerificationCodeException;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final UserRepository userRepository;

    @Transactional
    public void verifyCode(VerifyEmailCommand command) {
        User user = userRepository.findByEmail(command.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.getVerificationCode().equals(command.getCode())) {
            throw new VerificationCodeException("This code is not actual..");
        } else {
            user.setVerificationCode(null);
            user.setVerified(true);
            userRepository.save(user);
        }
    }


}
