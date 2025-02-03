package com.example.command;

import com.example.enums.Cryptocurrency;
import com.example.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateTransactionCommand {
    private String transactionId;
    private String transactionType;
    private String cryptocurrency;
    private BigDecimal price;
    private BigDecimal quantityInTransaction;
    private BigDecimal equivalentInUSD;



    public UpdateTransactionCommand(String transactionId,String transactionType, String cryptocurrency, BigDecimal price,
                                    BigDecimal quantityInTransaction, BigDecimal equivalentInUSD) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.cryptocurrency = cryptocurrency;
        this.price = price;
        this.quantityInTransaction = quantityInTransaction;
        this.equivalentInUSD = equivalentInUSD;
    }
}
