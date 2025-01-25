package com.example.controller;

import com.example.dto.UserDto;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/helloCurrentUser")
    public ResponseEntity<UserDto> getCurrentUser() {
        UserDto userDto = userService.getCurrentUserDto();
        return ResponseEntity.ok().body(userDto);
    }


    @GetMapping("/helloUser")
    public ResponseEntity<String> helloUser() {
        return ResponseEntity.ok("hello world");
    }

}
