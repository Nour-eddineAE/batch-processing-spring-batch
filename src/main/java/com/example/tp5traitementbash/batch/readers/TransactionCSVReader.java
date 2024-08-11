package com.example.tp5traitementbash.batch.readers;

import com.example.tp5traitementbash.entities.dtos.TransactionDTO;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component("CSVTransactionReader")
public class TransactionCSVReader extends FlatFileItemReader<TransactionDTO>{
    @Autowired
    public TransactionCSVReader(
            @Qualifier("transactionsCSVResource") Resource inputFileResource
    ) {
        this.setResource(inputFileResource);
        this.setLineMapper(lineMapper());
        this.setLinesToSkip(1);
    }

    public LineMapper<TransactionDTO> lineMapper() {
        DefaultLineMapper<TransactionDTO> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("idTransaction","idCompte","montant","dateTransaction");

        defaultLineMapper.setLineTokenizer(lineTokenizer);

        parseLine(defaultLineMapper);

        return defaultLineMapper;
    }

    protected void parseLine(DefaultLineMapper<TransactionDTO> defaultLineMapper) {
        defaultLineMapper.setFieldSetMapper(fieldSet -> {
            TransactionDTO transactionDTO = new TransactionDTO();

            transactionDTO.setTransactionId(fieldSet.readLong("idTransaction"));
            transactionDTO.setAccountId(fieldSet.readLong("idCompte"));
            transactionDTO.setAmount(fieldSet.readDouble("montant"));
            transactionDTO.setTransactionDate(fieldSet.readDate("dateTransaction"));

            return transactionDTO;
        });
    }
}
