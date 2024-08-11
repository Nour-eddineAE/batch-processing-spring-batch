package com.example.tp5traitementbash.services.transactions;

import com.example.tp5traitementbash.entities.Transaction;

import java.util.List;

public interface AbsTransactionService {
    Transaction saveTransaction(Transaction transaction);
    List<Transaction> saveAll(List<Transaction> transactions);
}
