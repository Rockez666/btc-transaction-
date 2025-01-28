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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_seq", allocationSize = 1)
    private Long id;
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

    public static String generateTransactionUUID() {
        Random random = new Random();
        int id = 10000 + random.nextInt(90000); // генерируем случайное число от 10000 до 99999
        return "#" + id;
    }

    public BigDecimal getEquivalentInTransaction() {
        return this.quantityInTransaction.multiply(this.price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction order = (Transaction) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

