package com.example.tp5traitementbash.services.transactions;

import com.example.tp5traitementbash.entities.Transaction;

public interface AbsTransactionService {
    Transaction saveTransaction(Transaction transaction);
}
