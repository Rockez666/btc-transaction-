package com.example.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.command.AuthorizationUserCommand;
import com.example.command.RegisterNewUserCommand;
import com.example.exception.ThatUserIsVerifiedException;
import com.example.exception.UserNotFoundException;
import com.example.service.AuthService;
import com.example.service.VerificationEmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final VerificationEmailService verificationService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody RegisterNewUserCommand registerCommand) {
        authService.createUser(registerCommand);
        return ResponseEntity.ok().body("Verification email sent successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthorizationUserCommand authorizationUserCommand) {
        String token = authService.login(authorizationUserCommand);
        return ResponseEntity.ok().body(token);
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        try {
            verificationService.verifyEmailToken(token);
            return ResponseEntity.ok().body("Email verified successfully");
        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        } catch (UserNotFoundException | ThatUserIsVerifiedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
