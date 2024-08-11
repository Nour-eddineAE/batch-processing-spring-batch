package com.example.tp5traitementbash.batch.writers;

import com.example.tp5traitementbash.entities.Transaction;
import com.example.tp5traitementbash.services.transactions.AbsTransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class TransactionWriter implements ItemWriter<Transaction> {
    private final AbsTransactionService transactionService;

    @Override
    public void write(Chunk<? extends Transaction> chunk) throws Exception {
        this.transactionService.saveAll((List<Transaction>) chunk.getItems());
    }
}
