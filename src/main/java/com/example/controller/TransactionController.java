package com.example.controller;

import com.example.command.CreateTransactionCommand;
import com.example.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/transactions")
@RequiredArgsConstructor

public class TransactionController {
    private final TransactionService transactionService;
    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping()
    ResponseEntity<String> createTransaction(@Valid @RequestBody CreateTransactionCommand command) {
        transactionService.createTransactionToUser(command);
        return ResponseEntity.ok("Transaction Created");
    }


}
