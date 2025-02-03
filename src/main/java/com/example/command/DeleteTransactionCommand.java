package com.example.command;

import lombok.Data;

@Data
public class DeleteTransactionCommand {
    private String transactionId;

    public DeleteTransactionCommand(String transactionId) {
        this.transactionId = transactionId;
    }
}
