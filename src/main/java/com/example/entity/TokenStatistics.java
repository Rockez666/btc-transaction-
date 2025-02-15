package com.example.entity;

import com.example.enums.Cryptocurrency;
import com.example.enums.TransactionType;
import com.example.exception.AmountMustBeGreaterThanZeroException;
import com.example.exception.NotEnoughQuantityToSellException;
import com.example.service.TransactionService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Getter
@Setter
public class TokenStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tokenStatistics_seq")
    @SequenceGenerator(name = "tokenStatistics_seq", sequenceName = "tokenStatistics_seq", allocationSize = 1)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Cryptocurrency cryptocurrency;
    private BigDecimal totalTokens;
    private BigDecimal averagePurchasePrice;
    private BigDecimal averageSellPrice;
    private BigDecimal equivalentCrypto;


    public TokenStatistics() {

    }

    public TokenStatistics(Cryptocurrency cryptocurrency, BigDecimal totalTokens, BigDecimal averagePurchasePrice, BigDecimal averageSellPrice, BigDecimal equivalentCrypto) {
        this.cryptocurrency = cryptocurrency;
        this.totalTokens = totalTokens;
        this.averagePurchasePrice = averagePurchasePrice;
        this.averageSellPrice = averageSellPrice;
        this.equivalentCrypto = equivalentCrypto;
    }

    public void withdrawTokens(BigDecimal amountTokens) {
        Optional.ofNullable(amountTokens)
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) > 0 && totalTokens.compareTo(amount) >= 0)
                .ifPresentOrElse(amount -> this.totalTokens = this.totalTokens.subtract(amount),
                        () -> {
                            throw new NotEnoughQuantityToSellException("Not enough quantity to trade");
                        });
    }

    public void addTokens(BigDecimal amountTokens) {
        Optional.ofNullable(amountTokens)
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) > 0)
                .ifPresentOrElse(amount -> this.totalTokens = this.totalTokens.add(amount),
                        () -> {
                            throw new AmountMustBeGreaterThanZeroException("Amount must be greater than zero");
                        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenStatistics that = (TokenStatistics) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
