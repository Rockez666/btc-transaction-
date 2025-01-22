package com.example.controller;

import com.example.command.AuthorizationUserCommand;
import com.example.command.VerifyEmailCommand;
import com.example.command.RegisterNewUserCommand;
import com.example.service.EmailService;
import com.example.service.UserService;
import com.example.service.VerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final VerificationService verificationService;
    private final EmailService emailService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody RegisterNewUserCommand registerCommand) {
        userService.createUser(registerCommand);
        return ResponseEntity.ok().body("Verification email sent successfully!");
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestBody VerifyEmailCommand command) {
        verificationService.verifyCode(command);
        return ResponseEntity.ok().body("Verification email sent successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthorizationUserCommand authorizationUserCommand) {
        Map<String, String> responseMap = userService.login(authorizationUserCommand);
        return ResponseEntity.ok().body(responseMap);
    }

    @GetMapping("/helloUser")
    public ResponseEntity<String> helloUser() {
        return ResponseEntity.ok("hello world");
    }

}
