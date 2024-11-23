package com.example.entity;

import com.example.enums.Cryptocurrency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

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

    public TokenStatistics() {
    }

    public TokenStatistics(Cryptocurrency cryptocurrency, BigDecimal totalTokens, BigDecimal averagePurchasePrice, BigDecimal averageSellPrice) {
        this.cryptocurrency = cryptocurrency;
        this.totalTokens = totalTokens;
        this.averagePurchasePrice = averagePurchasePrice;
        this.averageSellPrice = averageSellPrice;
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
