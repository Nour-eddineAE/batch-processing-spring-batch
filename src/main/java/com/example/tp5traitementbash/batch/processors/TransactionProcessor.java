package com.example.tp5traitementbash.batch.processors;

import com.example.tp5traitementbash.dtos.TransactionDTO;
import com.example.tp5traitementbash.entities.Transaction;
import com.example.tp5traitementbash.services.comptes.AbsComptesService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Calendar;


@RequiredArgsConstructor
@Component
public class TransactionProcessor implements ItemProcessor<TransactionDTO, Transaction> {
    private final AbsComptesService comptesService;

    @Override
    public Transaction process(TransactionDTO item) throws Exception {
        var transaction = new Transaction();
        transaction.setIdTransaction(item.getIdTransaction());
        transaction.setMontant(item.getMontant());
        transaction.setDateTransaction(item.getDateTransaction());

        // extract dateDebit
        var calendar = Calendar.getInstance();
        calendar.setTime(item.getDateTransaction());
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        transaction.setDateDebit(calendar.getTime());

        var compte = comptesService.getCompte(item.getIdCompte());
        compte.debiter(transaction.getMontant());

        transaction.setCompte(compte);

        return transaction;
    }
}
