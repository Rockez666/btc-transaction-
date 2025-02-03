package com.example.service;

import com.example.entity.User;
import com.example.exception.SamePasswordException;
import com.example.exception.ThatUserIsVerifiedException;
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
    // TODO: fix login and add button to send verif link if user is not verif but want to change password
    public void verifyResetPassword(String email, String verificationCode, String newPassword) {
        User userToReset = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        if (!userToReset.getVerificationCode().equals(verificationCode)) {
            throw new VerificationCodeException("Wrong verification code");
        }
        if (userToReset.getPassword().equals(newPassword)) {
            throw new SamePasswordException("Same password");
        }
        userToReset.setPassword(newPassword);
        userToReset.setVerificationCode(null);
    }


}
