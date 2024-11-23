package com.example.exception;

public class NotEnoughQuantityToSellException extends RuntimeException {
    public NotEnoughQuantityToSellException(String message) {
        super(message);
    }
}
