package com.example.entity;

import com.example.enums.Cryptocurrency;
import com.example.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction {
    @Id
    private String transactionId;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    private Cryptocurrency cryptocurrency;
    private BigDecimal price;
    private BigDecimal quantityInTransaction;
    private BigDecimal equivalentInUSD;
    private LocalDate creationDate = LocalDate.now();

    public Transaction() {
    }

    public Transaction(User user, TransactionType transactionType, Cryptocurrency cryptocurrency, BigDecimal price,
                       BigDecimal quantityInTransaction, BigDecimal equivalentInUSD) {
        this.transactionId = UUID.randomUUID().toString();
        this.user = user;
        this.transactionType = transactionType;
        this.cryptocurrency = cryptocurrency;
        this.price = price;
        this.quantityInTransaction = quantityInTransaction;
        this.equivalentInUSD = equivalentInUSD;
        this.creationDate = LocalDate.now();
    }

    public BigDecimal getEquivalentInTransaction() {
        return this.quantityInTransaction.multiply(this.price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction order = (Transaction) o;
        return Objects.equals(transactionId, order.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(transactionId);
    }
}

