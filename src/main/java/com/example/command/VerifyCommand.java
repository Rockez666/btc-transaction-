package com.example.command;

import lombok.Data;

@Data
public class VerifyCommand {
    private String email;
    private String code;

    public VerifyCommand(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public VerifyCommand() {
    }
}
