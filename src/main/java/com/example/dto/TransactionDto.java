package com.example.dto;

import com.example.enums.Cryptocurrency;
import com.example.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDto {
    private Long userId;
    private TransactionType transactionType;
    private Cryptocurrency cryptocurrency;
    private BigDecimal price;
    private BigDecimal quantityInTransaction;
    private BigDecimal equivalentInUSD;


    public TransactionDto() {
    }

    public TransactionDto(Long userId, TransactionType transactionType, Cryptocurrency cryptocurrency, BigDecimal price, BigDecimal quantity, BigDecimal equivalentInUSD) {
        this.userId = userId;
        this.transactionType = transactionType;
        this.cryptocurrency = cryptocurrency;
        this.price = price;
        this.quantityInTransaction = quantity;
        this.equivalentInUSD = equivalentInUSD;
    }
}
