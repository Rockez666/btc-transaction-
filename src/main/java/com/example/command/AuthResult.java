package com.example.command;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResult {
    private final boolean success;
    private final String message;


    private AuthResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static AuthResult success(String message) {
        return new AuthResult(true, message);
    }

    public static AuthResult failure(String message) {
        return new AuthResult(false, message);
    }

}
