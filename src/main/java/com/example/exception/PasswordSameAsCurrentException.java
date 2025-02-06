package com.example.exception;

public class PasswordSameAsCurrentException extends RuntimeException {
    public PasswordSameAsCurrentException(String message) {
        super(message);
    }
}
