package com.example.command;

import lombok.Data;

@Data
public class VerifyEmailCommand {
    private String email;
    private String code;
}
