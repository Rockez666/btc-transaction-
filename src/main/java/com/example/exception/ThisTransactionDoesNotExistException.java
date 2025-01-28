package com.example.exception;

public class ThisTransactionDoesNotExistException extends RuntimeException {

    public ThisTransactionDoesNotExistException(String message) {
        super(message);
    }
}
