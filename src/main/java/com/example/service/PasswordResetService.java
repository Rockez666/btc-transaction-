package com.example.service;

import com.example.command.ResetPasswordCommand;
import com.example.entity.User;
import com.example.exception.EmailNotFoundException;
import com.example.exception.PasswordSameAsCurrentException;
import com.example.exception.PasswordsDoNotMatchException;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;


    // переход на форму
    @Transactional
    public void sendLinkToResetPassword(String email) {
        User userToReset = userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException("Email not found"));
        String verificationCode = emailService.generateCode();
        userToReset.setVerificationCode(verificationCode);
        userRepository.save(userToReset);

        emailService.sendLinkToResetPassword(email, verificationCode);
    }

    // форма
    @Transactional
    public void resetPassword(ResetPasswordCommand passwordResetCommand) {
        String email = passwordResetCommand.getEmail();
        String newPassword = passwordResetCommand.getNewPassword();
        String newPasswordConfirm = passwordResetCommand.getNewPasswordConfirm();

        User userToReset = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("You could not be found, such email is not registered yet"));

        if (passwordEncoder.matches(newPassword, userToReset.getPassword())) {
            throw new PasswordSameAsCurrentException("New password should not be the same as the current password");
        }

        if (!newPassword.equals(newPasswordConfirm)) {
            throw new PasswordsDoNotMatchException("The new password does not match");
        }
        userToReset.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userToReset);
    }
}
