package com.example.service;

import com.example.command.CreateTransactionCommand;
import com.example.entity.TokenStatistics;
import com.example.entity.Transaction;
import com.example.entity.User;
import com.example.enums.Cryptocurrency;
import com.example.enums.TransactionType;
import com.example.exception.NotEnoughQuantityToSellException;
import com.example.exception.TransactionIsNullException;
import com.example.repository.TransactionRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createTransactionToUser(CreateTransactionCommand command) {
        User user = userService.getUserEntityById(command.getUserId());
        TokenStatistics tokenStatisticsUserReceives = getOrCreateTokenStatistics(user, command.getCryptocurrency());
        Transaction transaction = createTransaction(command);
        user.getTransactions().add(transaction);
        validateTransaction(command, tokenStatisticsUserReceives);

        updateTokenStatistics(user, tokenStatisticsUserReceives, command, transaction);
        userRepository.save(user);
        transactionRepository.save(transaction);
    }

    private Transaction createTransaction(CreateTransactionCommand command) {
        User user = userService.getUserEntityById(command.getUserId());
        TransactionType transactionType = TransactionType.getTransactionType(command.getTransactionType());
        Cryptocurrency cryptocurrency = Cryptocurrency.getCryptocurrency(command.getCryptocurrency());
        BigDecimal price = command.getPrice();
        BigDecimal quantity = command.getQuantity();
        return new Transaction(user, transactionType, cryptocurrency, price, quantity);

    }

    private void updateTokenStatistics(User user, TokenStatistics tokenStatistics, CreateTransactionCommand command, Transaction transaction) {
        if (transaction.getTransactionType() == TransactionType.BUY) {
            tokenStatistics.addTokens(command.getQuantity());
        } else if (transaction.getTransactionType() == TransactionType.SELL) {
            tokenStatistics.withdrawTokens(command.getQuantity());
        } 

        BigDecimal avgPurchasePrice = Transaction.calculateAveragePurchasePrice(user.getTransactions(), Cryptocurrency.getCryptocurrency(command.getCryptocurrency()));
        BigDecimal avgSellPrice = Transaction.calculateAverageSellPrice(user.getTransactions(), Cryptocurrency.getCryptocurrency(command.getCryptocurrency()));

        tokenStatistics.setAveragePurchasePrice(avgPurchasePrice);
        tokenStatistics.setAverageSellPrice(avgSellPrice);

    }

    private TokenStatistics getOrCreateTokenStatistics(User user, String cryptocurrency) {
        Cryptocurrency cryptoEnum = Cryptocurrency.getCryptocurrency(cryptocurrency);
        return user.getTokenStatistics().stream()
                .filter(statistics -> statistics.getCryptocurrency().equals(cryptoEnum))
                .findFirst()
                .orElseGet(() -> createNewTokenStatistics(user, cryptoEnum));
    }

    private TokenStatistics createNewTokenStatistics(User userReceives, Cryptocurrency cryptoEnum) {
        TokenStatistics tokenStatistics = new TokenStatistics(cryptoEnum, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        userReceives.getTokenStatistics().add(tokenStatistics);
        return tokenStatistics;
    }

    private void validateTransaction(CreateTransactionCommand command, TokenStatistics tokenStatisticsUserReceives) {
        TransactionType transactionType = TransactionType.getTransactionType(command.getTransactionType());
        if (transactionType == TransactionType.SELL) {
            if (tokenStatisticsUserReceives.getTotalTokens().compareTo(command.getQuantity()) < 0) {
                throw new NotEnoughQuantityToSellException("Not enough quantity to sell");
            }
        }
    }
}

