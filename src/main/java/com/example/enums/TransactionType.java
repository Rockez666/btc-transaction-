package com.example.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TransactionType {
    BUY("Buy/MARKET"),
    SELL("Sell/MARKET");


    private final String transaction;

    TransactionType(String transaction) {
        this.transaction = transaction;
    }

    public static TransactionType getTransactionType(String transaction) {
        return Arrays.stream(TransactionType.values())
                .filter(value -> value.getTransaction().equalsIgnoreCase(transaction))
                .findFirst()
                .orElse(null);
    }
}
