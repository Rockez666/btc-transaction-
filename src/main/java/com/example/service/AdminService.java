package com.example.service;

import com.example.command.UpdateTransactionCommand;
import com.example.command.UserUpdateCommand;
import com.example.dto.UserDto;
import com.example.entity.Transaction;
import com.example.entity.User;
import com.example.enums.Cryptocurrency;
import com.example.enums.TransactionType;
import com.example.exception.ThisTransactionDoesNotExistException;
import com.example.exception.UserNotFoundException;
import com.example.mapper.UserMapper;
import com.example.repository.TransactionRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final UserMapper userMapper;


    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

//    @Transactional
//    public void updateUser(@RequestBody String userId, @RequestBody UserUpdateCommand command) {
//        User userToUpdate = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
//        if (command.getUsername() != null) {
//            userToUpdate.setUsername(command.getUsername());
//        }
//        if (command.getPassword() != null) {
//            userToUpdate.setPassword(command.getPassword());
//        }
//        userRepository.save(userToUpdate);
//    }

    @Transactional
    public void updateTransaction(UpdateTransactionCommand command) {
        Transaction transactionToUpdate = transactionRepository.findByTransactionId(command.getTransactionId()).orElseThrow(() -> new ThisTransactionDoesNotExistException("transaction not found"));
        replacingTransactionData(transactionToUpdate, command);
    }

    private void replacingTransactionData(Transaction transactionToUpdate, UpdateTransactionCommand command) {
        TransactionType transactionType = TransactionType.getTransactionType(command.getTransactionType());
        Cryptocurrency cryptocurrency = Cryptocurrency.getCryptocurrency(command.getCryptocurrency());
        BigDecimal price = command.getPrice();
        BigDecimal quantityInTransaction = command.getQuantityInTransaction();
        BigDecimal equivalentInUSD = command.getEquivalentInUSD();

        if (transactionType != null && transactionType != transactionToUpdate.getTransactionType()) {
            transactionToUpdate.setTransactionType(transactionType);
        }
        if (cryptocurrency != null && cryptocurrency != transactionToUpdate.getCryptocurrency()) {
            transactionToUpdate.setCryptocurrency(cryptocurrency);
        }
        if (price != null && price != transactionToUpdate.getPrice()) {
            transactionToUpdate.setPrice(price);
        }
        if (quantityInTransaction != null && quantityInTransaction != transactionToUpdate.getQuantityInTransaction()) {
            transactionToUpdate.setQuantityInTransaction(quantityInTransaction);
        }
        if (equivalentInUSD != null && equivalentInUSD != transactionToUpdate.getEquivalentInUSD()) {
            transactionToUpdate.setEquivalentInUSD(equivalentInUSD);
        }
        transactionRepository.save(transactionToUpdate);
    }



}
