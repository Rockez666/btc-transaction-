package com.example.controller;

import com.example.command.CreateTransactionCommand;
import com.example.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/transactions")
@RequiredArgsConstructor

public class TransactionController {
    private final TransactionService transactionService;


    @PostMapping("/createTransaction")
    public ResponseEntity<String> createTransaction(@Valid @RequestBody CreateTransactionCommand command) {
        transactionService.createTransaction(command);
        return ResponseEntity.ok("Transaction Created");
    }
}



