package com.example.exception;

public class EmailIsNotVerified extends RuntimeException {
    public EmailIsNotVerified(String message) {
        super(message);
    }
}
