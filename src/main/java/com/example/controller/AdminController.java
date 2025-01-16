package com.example.controller;

import com.example.command.UpdateUserPasswordCommand;
import com.example.dto.UserDto;
import com.example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/adminPanel")
public class AdminController {
    private final UserService userService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello admin");
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser() {
        return ResponseEntity.ok().body(userService.getAllUsers());
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

//    @PutMapping()
//    public ResponseEntity<String> updateUserData(@Valid @RequestBody UpdateUserPasswordCommand updateCommand) {
//        userService.updateUser(updateCommand);
//        return ResponseEntity.ok().body("User updated");
//    }


}
