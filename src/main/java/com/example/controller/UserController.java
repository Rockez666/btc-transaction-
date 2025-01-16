package com.example.controller;

import com.example.command.AuthorizationUserCommand;
import com.example.command.RegisterNewUserCommand;
import com.example.service.UserService;
import com.example.utill.JWTUtill;
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

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody RegisterNewUserCommand registerCommand) {
        userService.createUser(registerCommand);

        return ResponseEntity.ok().body("Successfully registered!");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthorizationUserCommand authorizationUserCommand) {
        Map<String, String> responseMap = userService.login(authorizationUserCommand);
        return ResponseEntity.ok().body(responseMap);
    }

}
