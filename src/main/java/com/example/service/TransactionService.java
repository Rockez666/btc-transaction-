package com.example.service;

import com.example.command.CreateTransactionCommand;
import com.example.entity.*;
import com.example.enums.Cryptocurrency;
import com.example.enums.TransactionType;
import com.example.exception.NotEnoughQuantityToSellException;
import com.example.exception.TransactionIsNullException;
import com.example.repository.TransactionRepository;
import com.example.repository.UserRepository;
import com.example.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CryptoCalculate cryptoCalculate;

    @Transactional
    public void createTransactionToUser(CreateTransactionCommand command) {
        User currentUser = userService.getCurrentUser();

        List<Transaction> transactions = currentUser.getTransactions();
        TransactionType transactionType = TransactionType.getTransactionType(command.getTransactionType());
        Cryptocurrency cryptocurrency = Cryptocurrency.getCryptocurrency(command.getCryptocurrency());

        BigDecimal price = command.getPrice();
        BigDecimal quantity = command.getQuantity();
        Transaction transaction = createTransaction(transactionType, cryptocurrency, price, quantity, currentUser);

        TokenStatistics tokenStatisticsUserRecipient = getOrCreateTokenStatistics(currentUser, cryptocurrency);

        currentUser.getTransactions().add(transaction);
        validateTransaction(command, tokenStatisticsUserRecipient);
        updateTokenStatistics(transaction, tokenStatisticsUserRecipient, quantity, cryptocurrency, transactions);
        userRepository.save(currentUser);
        transactionRepository.save(transaction);
    }

    private Transaction createTransaction(TransactionType transactionType, Cryptocurrency cryptocurrency,
                                          BigDecimal price, BigDecimal quantity, User user) {
        if (transactionType == TransactionType.BUY || transactionType == TransactionType.SELL) {
            return new Transaction(user, transactionType, cryptocurrency, price, quantity, BigDecimal.ZERO);
        } else {
            throw new TransactionIsNullException("Error creating transaction: transaction is null");
        }
    }

    private void updateTokenStatistics(Transaction transaction, TokenStatistics tokenStatistics, BigDecimal quantity,
                                       Cryptocurrency cryptocurrency, List<Transaction> userTransactions) {
        if (transaction.getTransactionType() == TransactionType.BUY) {
            tokenStatistics.addTokens(quantity);
        } else if (transaction.getTransactionType() == TransactionType.SELL) {
            tokenStatistics.withdrawTokens(quantity);
        }
        BigDecimal avgPurchasePrice = cryptoCalculate.calculateAveragePurchasePrice(userTransactions, cryptocurrency);
        BigDecimal avgSellPrice = cryptoCalculate.calculateAverageSellPrice(userTransactions, cryptocurrency);
        BigDecimal equivalentCrypto = cryptoCalculate.getEquivalentUsdForCrypto(userTransactions, cryptocurrency);
        transaction.setEquivalentInUSD(transaction.getEquivalentInTransaction());
        tokenStatistics.setAveragePurchasePrice(avgPurchasePrice);
        tokenStatistics.setAverageSellPrice(avgSellPrice);
        tokenStatistics.setEquivalentCrypto(equivalentCrypto);


    }

    private TokenStatistics getOrCreateTokenStatistics(User mainUser, Cryptocurrency cryptocurrency) {
        return mainUser.getTokenStatistics().stream()
                .filter(statistics -> statistics.getCryptocurrency().equals(cryptocurrency))
                .findFirst()
                .orElseGet(() -> createNewTokenStatistics(mainUser, cryptocurrency));
    }

    private TokenStatistics createNewTokenStatistics(User mainUser, Cryptocurrency cryptoEnum) {
        TokenStatistics tokenStatistics = new TokenStatistics(cryptoEnum, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        mainUser.getTokenStatistics().add(tokenStatistics);
        return tokenStatistics;
    }

    private void validateTransaction(CreateTransactionCommand command, TokenStatistics tokenStatisticsUserSender) {
        TransactionType transactionType = TransactionType.getTransactionType(command.getTransactionType());
        if (transactionType == TransactionType.SELL) {
            if (tokenStatisticsUserSender.getTotalTokens().compareTo(command.getQuantity()) < 0) {
                throw new NotEnoughQuantityToSellException("Not enough quantity to sell");
            }
        }

    }
}

