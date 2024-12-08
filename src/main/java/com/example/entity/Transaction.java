package com.example.entity;

import com.example.enums.Cryptocurrency;
import com.example.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_seq", allocationSize = 1)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    private Cryptocurrency cryptocurrency;
    private BigDecimal price;
    private BigDecimal quantityInTransaction;
    private LocalDate creationDate = LocalDate.now();

    public Transaction() {
    }

   
    public Transaction(User user, TransactionType transactionType, Cryptocurrency cryptocurrency, BigDecimal price, BigDecimal quantityInTransaction) {
        this.user = user;
        this.transactionType = transactionType;
        this.cryptocurrency = cryptocurrency;
        this.price = price;
        this.quantityInTransaction = quantityInTransaction;
        this.creationDate = LocalDate.now();
    }


    public static BigDecimal calculateAveragePurchasePrice(List<Transaction> transactions, Cryptocurrency cryptocurrency) {
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalQuantity = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            if (transaction.getCryptocurrency().equals(cryptocurrency) && transaction.getTransactionType() == TransactionType.BUY) {
                totalCost = totalCost.add(transaction.getPrice().multiply(transaction.getQuantityInTransaction()));
                totalQuantity = totalQuantity.add(transaction.getQuantityInTransaction());
            }
        }
        if (totalQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return totalCost.divide(totalQuantity, 2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal calculateAverageSellPrice(List<Transaction> transactions, Cryptocurrency cryptocurrency) {
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalQuantity = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            if (transaction.getCryptocurrency().equals(cryptocurrency) && transaction.getTransactionType() == TransactionType.SELL) {
                totalCost = totalCost.add(transaction.getPrice().multiply(transaction.getQuantityInTransaction()));
                totalQuantity = totalQuantity.add(transaction.getQuantityInTransaction());
            }
        }
        if (totalQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return totalCost.divide(totalQuantity, 2, BigDecimal.ROUND_HALF_UP);
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

