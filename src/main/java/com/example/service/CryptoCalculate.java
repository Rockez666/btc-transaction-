package com.example.service;

import com.example.entity.TokenStatistics;
import com.example.entity.Transaction;
import com.example.enums.Cryptocurrency;
import com.example.enums.TransactionType;
import com.example.exception.AmountMustBeGreaterThanZeroException;
import com.example.exception.ListIsEmptyException;
import lombok.RequiredArgsConstructor;
import org.apache.el.lang.ELArithmetic;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoCalculate {

    public BigDecimal calculateAveragePurchasePrice(List<Transaction> transactions, Cryptocurrency cryptocurrency) {
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalQuantity = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            if (transaction.getCryptocurrency() == cryptocurrency && transaction.getTransactionType() == TransactionType.BUY) {
                totalCost = totalCost.add(transaction.getPrice().multiply(transaction.getQuantityInTransaction()));
                totalQuantity = totalQuantity.add(transaction.getQuantityInTransaction());
            }
        }
        if (totalQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return totalCost.divide(totalQuantity, 2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal calculateAverageSellPrice(List<Transaction> transactions, Cryptocurrency cryptocurrency) {
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalQuantity = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            if (transaction.getCryptocurrency() == cryptocurrency && transaction.getTransactionType() == TransactionType.SELL) {
                totalCost = totalCost.add(transaction.getPrice().multiply(transaction.getQuantityInTransaction()));
                totalQuantity = totalQuantity.add(transaction.getQuantityInTransaction());
            }
        }
        if (totalQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return totalCost.divide(totalQuantity, 2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getEquivalentUsdForCrypto(List<Transaction> transactions, Cryptocurrency cryptocurrency) {
        BigDecimal totalTokens = BigDecimal.ZERO;
        BigDecimal totalEquivalentInUsd = BigDecimal.ZERO;
        if (transactions.isEmpty()) {
            throw new ListIsEmptyException("List of transactions is empty");
        } else {
            for (Transaction transaction : transactions) {
                if (transaction.getCryptocurrency() == cryptocurrency) {
                    BigDecimal transactionValueInUsd = transaction.getEquivalentInTransaction();

                    if (transaction.getTransactionType() == TransactionType.BUY) {
                        totalTokens = totalTokens.add(transaction.getQuantityInTransaction());
                        totalEquivalentInUsd = totalEquivalentInUsd.add(transactionValueInUsd);
                    } else if (transaction.getTransactionType() == TransactionType.SELL) {
                        totalTokens = totalTokens.subtract(transaction.getQuantityInTransaction());
                        totalEquivalentInUsd = totalEquivalentInUsd.subtract(transactionValueInUsd);
                    }
                }
            }
        }
        return totalEquivalentInUsd;
    }
}
