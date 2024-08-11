package com.example.tp5traitementbash.batch.processors;

import com.example.tp5traitementbash.entities.Transaction;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

@Service("transactionDebitProcessor")
public class TransactionDebitProcessor implements ItemProcessor<Transaction, Transaction> {
    @Override
    public Transaction process(Transaction transaction) throws Exception {
        var account = transaction.getAccount();

        account.debit(transaction.getAmount());

        return transaction;
    }
}
