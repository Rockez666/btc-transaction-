package com.example.service;

import com.example.entity.User;
import com.example.exception.ThatUserIsVerifiedException;
import com.example.exception.UserNotFoundException;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerificationEmailService {

    private final UserRepository userRepository;

    @Transactional
    public void verifyEmail(String email, String verificationCode) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        if (user.isVerified()) {
            throw new ThatUserIsVerifiedException("That user is already verified");
        } else if (user.getVerificationCode().equals(verificationCode)) {
            user.setVerified(true);
            user.setVerificationCode(null);
            userRepository.save(user);
        }

    }


}
