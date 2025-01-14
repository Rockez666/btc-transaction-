package com.example.controller;

import com.example.command.AuthorizationUserCommand;
import com.example.command.RegisterNewUserCommand;
import com.example.command.UpdateUserPasswordCommand;
import com.example.dto.UserDto;
import com.example.service.UserService;
import com.example.utill.JWTUtill;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JWTUtill jwtUtill;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody RegisterNewUserCommand registerCommand) {
        userService.createUser(registerCommand);

        return ResponseEntity.ok().body("Successfully registered!");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthorizationUserCommand authorizationUserCommand) {
        return ResponseEntity.ok().body(userService.login(authorizationUserCommand));
    }


    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }


    @GetMapping("/showUserInfo")
    public ResponseEntity<String> getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return ResponseEntity.ok(userDetails.getUsername());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto userDto = userService.getUserDtoById(id);
        return ResponseEntity.ok().body(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserByID(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().body("User deleted");
    }

    @PutMapping()
    public ResponseEntity<String> updateUserData(@Valid @RequestBody UpdateUserPasswordCommand updateCommand) {
        userService.updateUser(updateCommand);
        return ResponseEntity.ok().body("User updated");
    }

}
