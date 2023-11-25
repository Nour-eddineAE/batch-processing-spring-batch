package com.example.tp5traitementbash.services.transactions;

import com.example.tp5traitementbash.entities.Transaction;
import com.example.tp5traitementbash.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConcreteTransactionService implements AbsTransactionService{
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
