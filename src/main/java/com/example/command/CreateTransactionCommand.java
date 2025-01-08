package com.example.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateTransactionCommand {
    @NotBlank(message = "transactionType is mandatory")
    private String transactionType;
    @NotBlank(message = "cryptocurrency is mandatory")
    private String cryptocurrency;
    @Positive(message = "price should be positive")
    private BigDecimal price;
    @Positive(message = "quantity should be positive")
    private BigDecimal quantity;
    private LocalDate creationDate;

    public CreateTransactionCommand() {
        this.creationDate = LocalDate.now();
    }

    public CreateTransactionCommand(String transactionType, String cryptocurrency, BigDecimal price, BigDecimal quantity) {

        this.transactionType = transactionType;
        this.cryptocurrency = cryptocurrency;
        this.price = price;
        this.quantity = quantity;
        this.creationDate = LocalDate.now();
    }

}
