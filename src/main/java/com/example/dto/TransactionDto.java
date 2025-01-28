package com.example.dto;

import com.example.entity.Transaction;
import com.example.enums.Cryptocurrency;
import com.example.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private String transactionId;
    private TransactionType transactionType;
    private Cryptocurrency cryptocurrency;
    private BigDecimal price;
    private BigDecimal quantityInTransaction;
    private BigDecimal equivalentInUSD;
    private LocalDateTime transactionDate;


    public TransactionDto() {
    }

    public TransactionDto(String transactionId, TransactionType transactionType, Cryptocurrency cryptocurrency, BigDecimal price, BigDecimal quantity, BigDecimal equivalentInUSD) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.cryptocurrency = cryptocurrency;
        this.price = price;
        this.quantityInTransaction = quantity;
        this.equivalentInUSD = equivalentInUSD;
        this.transactionDate = LocalDateTime.now();
    }
}
