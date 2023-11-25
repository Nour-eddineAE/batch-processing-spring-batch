package com.example.tp5traitementbash.batch;

import com.example.tp5traitementbash.dtos.TransactionDTO;
import com.example.tp5traitementbash.entities.Transaction;
import com.example.tp5traitementbash.services.comptes.AbsComptesService;
import com.example.tp5traitementbash.services.transactions.AbsTransactionService;
import jakarta.transaction.Transactional;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.BindException;

import javax.sql.DataSource;
import java.util.Date;

@Configuration
//@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private AbsComptesService comptesService;
    @Autowired
    private AbsTransactionService transactionService;

    @Bean
    public Resource inputFileResource() {
        return new FileSystemResource("src\\main\\resources\\transactions.csv");
    }

    @Bean
    public LineMapper<TransactionDTO> lineMapper() {
        DefaultLineMapper<TransactionDTO> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("idTransaction","idCompte","montant","dateTransaction");

        defaultLineMapper.setLineTokenizer(lineTokenizer);

        defaultLineMapper.setFieldSetMapper(fieldSet -> {
            TransactionDTO transactionDTO = new TransactionDTO();

            transactionDTO.setIdTransaction(fieldSet.readLong("idTransaction"));
            transactionDTO.setIdCompte(fieldSet.readLong("idCompte"));
            transactionDTO.setMontant(fieldSet.readDouble("montant"));
            transactionDTO.setDateTransaction(fieldSet.readDate("dateTransaction"));

            return transactionDTO;
        });

        return defaultLineMapper;
    }

    @Bean
    public ItemReader<? extends TransactionDTO> reader() {
        FlatFileItemReader<TransactionDTO> reader = new FlatFileItemReader<TransactionDTO>();
        reader.setResource(inputFileResource());
        reader.setLineMapper(lineMapper());
        reader.setLinesToSkip(1);

        return reader;
    }

    @Bean
    public ItemProcessor<TransactionDTO, Transaction> itemProcessor() {
        return new ItemProcessor<TransactionDTO, Transaction>() {
            @Override
            public Transaction process(TransactionDTO item) throws Exception {
                var transaction = new Transaction();
                transaction.setIdTransaction(item.getIdTransaction());
                transaction.setMontant(item.getMontant());
                transaction.setDateTransaction(item.getDateTransaction());
                transaction.setDateDebit(new Date());

                var compte = comptesService.getCompte(item.getIdCompte());
                transaction.setCompte(compte);

                return transaction;
            }
        };
    }

    ;

    @Bean
    public ItemWriter<Transaction> writer() {
        return new ItemWriter<Transaction>() {
            @Override
            @Transactional
            public void write(Chunk<? extends Transaction> chunk) throws Exception {
                for (var transaction : chunk) {
                    transactionService.saveTransaction(transaction);

                    var compte = transaction.getCompte();
                    comptesService.debiterCompte(compte, transaction.getMontant());
                }
            }
        };
    }

    @Bean(name = "jobRepository")
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean(name = "jobLauncher")
    public JobLauncher jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher
                = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        return jobLauncher;
    }

    @Bean
    public Step step1() throws Exception {
        return new StepBuilder("importTransaction", jobRepository())
                .<TransactionDTO, Transaction>chunk(4, transactionManager)
                .reader(reader())
                .processor(itemProcessor())
                .writer(writer())
                .build();
    }

    @Bean(name = "importTransactions")
    public Job importTransactions() throws Exception {
        return new JobBuilder("importTransactions", jobRepository())
                .start(step1())
                .build();
    }
}
