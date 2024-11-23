package com.example.exception;

public class TransactionTypeNotFoundException extends RuntimeException {

    public TransactionTypeNotFoundException(String message) {
        super(message);
    }
}
