package com.example.controller;

import com.example.command.AuthorizationUserCommand;
import com.example.command.CreateUserCommand;
import com.example.command.UpdateUserCommand;
import com.example.dto.UserDto;
import com.example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin("*")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserCommand createCommand) {
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

    @GetMapping("/getUserAuthorization")
    public ResponseEntity<UserDto> getUserAuthorization(@Valid @RequestBody AuthorizationUserCommand authorizationCommand) {
        UserDto userDto = userService.getUserByAuthorization(authorizationCommand);
        return ResponseEntity.ok().body(userDto);
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
