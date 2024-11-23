package com.example.controller;

import com.example.command.CreateTransactionCommand;
import com.example.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor

public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping()
    ResponseEntity<String> createTransaction(@Valid @RequestBody CreateTransactionCommand command) {
        transactionService.createTransactionToUser(command);
        return ResponseEntity.ok("Transaction Created");
    }


}
