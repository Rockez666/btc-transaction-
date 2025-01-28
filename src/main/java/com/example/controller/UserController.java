package com.example.controller;

import com.example.dto.UserDto;
import com.example.service.TransactionService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TransactionService transactionService;

    @GetMapping("/currentUserDto")
    public ResponseEntity<UserDto> getCurrentUser() {
        UserDto userDto = userService.getCurrentUserDto();
        return ResponseEntity.ok().body(userDto);
    }

    @GetMapping("/helloUser")
    public ResponseEntity<String> helloUser() {
        return ResponseEntity.ok("hello world");
    }

    @DeleteMapping("/deleteTransaction")
    public ResponseEntity<String> deleteTransaction(@RequestBody String transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok("transaction deleted");
    }
}
