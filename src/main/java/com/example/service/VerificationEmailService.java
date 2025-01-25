package com.example.service;

import com.example.entity.User;
import com.example.exception.ThatUserIsVerifiedException;
import com.example.exception.UserNotFoundException;
import com.example.repository.UserRepository;
import com.example.utill.JWTUtill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class VerificationEmailService {

    private final UserRepository userRepository;
    private final AuthService authService;

    private final JWTUtill jwtUtill;

    @Transactional
    public void verifyEmailToken(String token) {
        Map<String, String> claims = jwtUtill.validateTokenAndRetrieveClaim(token);
        String email = claims.get("email");
        verifyUserByEmail(email);

    }

    private void verifyUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        if (user.isVerified()) {
            throw new ThatUserIsVerifiedException("You are already verified");
        }

        user.setVerified(true);
        userRepository.save(user);
    }


}
