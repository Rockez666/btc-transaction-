package com.example.enums;

import com.example.exception.TransactionTypeNotFoundException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TransactionType {
    BUY("Buy"),
    SELL("Sell");

    private final String transaction;

    TransactionType(String transaction) {
        this.transaction = transaction;
    }

    public static TransactionType getTransactionType(String transaction) {
        return Arrays.stream(TransactionType.values())
                .filter(value -> value.getTransaction().equalsIgnoreCase(transaction))
                .findFirst()
                .orElseThrow(() -> new TransactionTypeNotFoundException("Transaction type not found") );
    }
}
