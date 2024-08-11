package com.example.tp5traitementbash.batch;


import com.example.tp5traitementbash.batch.readers.TransactionReaderLoggingDecorator;
import com.example.tp5traitementbash.entities.dtos.TransactionDTO;
import com.example.tp5traitementbash.entities.Transaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
public class BatchConfig {
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;
    private final ItemStreamReader<TransactionDTO> transactionCSVReader;
    private final ItemStreamReader<TransactionDTO> transactionXMLReader;
    private final ItemProcessor<TransactionDTO, Transaction> transactionModelProcessor;
    private final ItemProcessor<Transaction, Transaction> transactionDebitProcessor;
    private final ItemWriter<Transaction> transactionWriter;

    public BatchConfig(
            PlatformTransactionManager transactionManager,
            DataSource dataSource,
            @Qualifier("CSVTransactionReader") ItemStreamReader<TransactionDTO> transactionCSVReader,
            @Qualifier("XMLTransactionReader") ItemStreamReader<TransactionDTO> transactionXMLReader,
            ItemProcessor<TransactionDTO, Transaction> transactionModelProcessor,
            ItemProcessor<Transaction, Transaction> transactionDebitProcessor,
            ItemWriter<Transaction> transactionWriter) {
        this.transactionManager = transactionManager;
        this.dataSource = dataSource;
        this.transactionCSVReader = transactionCSVReader;
        this.transactionXMLReader = transactionXMLReader;
        this.transactionModelProcessor = transactionModelProcessor;
        this.transactionDebitProcessor = transactionDebitProcessor;
        this.transactionWriter = transactionWriter;
    }

    @Bean
    public ItemProcessor<TransactionDTO, Transaction> compositeItemProcessor() {
        CompositeItemProcessor<TransactionDTO, Transaction> compositeItemProcessor = new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(Arrays.asList(this.transactionModelProcessor, this.transactionDebitProcessor));
        return compositeItemProcessor;
    }

    @Bean
    public Step step1() throws Exception {
        return new StepBuilder("importCSVTransaction", jobRepository())
                .<TransactionDTO, Transaction>chunk(10, this.transactionManager)
                .reader(new TransactionReaderLoggingDecorator(this.transactionCSVReader))
                .processor(compositeItemProcessor())
                .writer(this.transactionWriter)
                .build();
    }

    @Bean
    public Step step2() throws Exception {
        return new StepBuilder("importXMLTransaction", jobRepository())
                .<TransactionDTO, Transaction>chunk(10, this.transactionManager)
                .reader(new TransactionReaderLoggingDecorator( this.transactionXMLReader))
                .processor(compositeItemProcessor())
                .writer(this.transactionWriter)
                .build();
    }

    @Bean
    public Flow flow1() throws Exception {
        return new FlowBuilder<Flow>("flow1")
                .start(step1())
                .build();
    }

    @Bean
    public Flow flow2() throws Exception {
        return new FlowBuilder<Flow>("flow1")
                .start(step2())
                .build();
    }

    @Bean
    public Flow splitFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(flow1(), flow2())
                .build();
    }

    @Bean(name = "importTransactions")
    public Job importTransactions() throws Exception {
        return new JobBuilder("importTransactions", jobRepository())
                .start(splitFlow())
                .build()
                .build();
    }

    @Bean(name = "jobRepository")
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(this.dataSource);
        factory.setTransactionManager(this.transactionManager);
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
}
