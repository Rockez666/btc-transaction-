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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserByID(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok().body("User successfully deleted");
    }

    @PostMapping("/updateTransaction")
    public ResponseEntity<String> updateTransaction(@RequestBody UpdateTransactionCommand command) {
        adminService.updateTransaction(command);
        return ResponseEntity.ok().body("Transaction successfully updated");
    }

}
