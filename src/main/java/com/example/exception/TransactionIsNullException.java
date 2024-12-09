package com.example.exception;

public class TransactionIsNullException extends RuntimeException {
    public TransactionIsNullException(String message) {
        super(message);
    }
}
