package com.example.controller;

import com.example.command.AuthorizationUserCommand;
import com.example.command.RegisterNewUserCommand;
import com.example.exception.ThatUserIsVerifiedException;
import com.example.exception.UserNotFoundException;
import com.example.service.AuthService;
import com.example.service.VerificationService;
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
    private final VerificationService verificationService;

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
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email, @RequestParam("code") String code) {
        try {
            verificationService.verifyEmail(email,code);
            return ResponseEntity.ok().body("Email verified successfully");
        } catch (UserNotFoundException | ThatUserIsVerifiedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
