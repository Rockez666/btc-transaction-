package com.example.exception;

public class ThisUsernameOrEmailAlreadyExists extends RuntimeException {

    public ThisUsernameOrEmailAlreadyExists(String message) {
        super(message);
    }
}
