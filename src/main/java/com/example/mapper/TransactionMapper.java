package com.example.mapper;

import com.example.dto.TransactionDto;
import com.example.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionMapper {

    public TransactionDto transactionToDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        return new TransactionDto(
                transaction.getId(),
                transaction.getTransactionType(),
                transaction.getCryptocurrency(),
                transaction.getPrice(),
                transaction.getQuantityInTransaction(),
                transaction.getEquivalentInUSD()
                );

    }
}
