package com.example.controller;

import com.example.command.UpdateTransactionCommand;
import com.example.command.UpdateUserPasswordCommand;
import com.example.dto.UserDto;
import com.example.service.AdminService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/adminPanel")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello admin");
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUser() {
        return ResponseEntity.ok().body(adminService.getAllUsers());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserByID(@PathVariable String userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok().body("User successfully deleted");
    }
}
