package com.example.tp5traitementbash.batch.readers;

import com.example.tp5traitementbash.entities.dtos.TransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.*;
public class TransactionReaderLoggingDecorator implements ItemStreamReader<TransactionDTO>{
    private static Logger logger = LoggerFactory.getLogger("SampleLogger");
    private final ItemStreamReader<TransactionDTO> transactionReader;

    public TransactionReaderLoggingDecorator(ItemStreamReader<TransactionDTO> transactionReader) {
        this.transactionReader = transactionReader;
    }

    @Override
    public TransactionDTO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        try{
            var transactionDTO = this.transactionReader.read();

            // prevent accessing a null reference
            if (transactionDTO == null)
                return null;

            String message = "Transaction ID: " +
                    transactionDTO.getTransactionId() +
                    "\t Account ID: " +
                    transactionDTO.getAccountId() +
                    "\t Amount: " +
                    transactionDTO.getAmount() +
                    "\t Transaction Date: " +
                    transactionDTO.getTransactionDate();

            logger.info(message);

            return transactionDTO;
        } catch (Exception exception) {
            String errorMsg = "Error reading transaction: " + exception.getMessage();

            logger.error(errorMsg);

            throw  new RuntimeException(errorMsg);
        }
    }


    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.transactionReader.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        this.transactionReader.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        this.transactionReader.close();
    }
}
