package com.example.command;

import lombok.Data;

@Data
public class EmailRequest {
    private String email;

    public EmailRequest(String email) {
        this.email = email;
    }
}
