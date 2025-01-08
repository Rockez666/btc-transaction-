package com.example.controller;

import com.example.command.AuthResult;
import com.example.command.AuthorizationUserCommand;
import com.example.command.RegisterNewUserCommand;
import com.example.command.UpdateUserCommand;
import com.example.dto.UserDto;
import com.example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody RegisterNewUserCommand createCommand) {
        userService.createUser(createCommand);
        return ResponseEntity.ok().body("User created");
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto userDto = userService.getUserDtoById(id);
        return ResponseEntity.ok().body(userDto);
    }

    @GetMapping("/auth")
    public ResponseEntity<String> auth(@Valid @RequestBody AuthorizationUserCommand authorizationCommand) {
        AuthResult authResult = userService.auth(authorizationCommand);
        return authResult.isSuccess() ? ResponseEntity.ok(authResult.getMessage()) : ResponseEntity.status(401).body(authResult.getMessage());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserByID(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().body("User deleted");
    }

    @PutMapping()
    public ResponseEntity<String> updateUserDetails(@Valid @RequestBody UpdateUserCommand updateCommand) {
        userService.updateUser(updateCommand);
        return ResponseEntity.ok().body("User updated");
    }

}
