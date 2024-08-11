package com.example.tp5traitementbash.batch.processors;

import com.example.tp5traitementbash.entities.dtos.TransactionDTO;
import com.example.tp5traitementbash.entities.Transaction;
import com.example.tp5traitementbash.services.comptes.AbsAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import java.util.Calendar;


@RequiredArgsConstructor
@Service("transactionModelProcessor")
public class TransactionModelProcessor implements ItemProcessor<TransactionDTO, Transaction> {
    private final AbsAccountService accountService;

    @Override
    public Transaction process(TransactionDTO transactionDTO) throws Exception {
        var transaction = new Transaction();
        transaction.setTransactionId(transactionDTO.getTransactionId());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionDate(transactionDTO.getTransactionDate());

        // extract dateDebit
        var calendar = Calendar.getInstance();
        calendar.setTime(transactionDTO.getTransactionDate());
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        transaction.setDebitDate(calendar.getTime());

        var account = this.accountService.getAccount(transactionDTO.getAccountId());

        transaction.setAccount(account);

        return transaction;
    }
}
