package com.example.tp5traitementbash.resources;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class TransactionResourceConfig {
    @Bean("transactionsCSVResource")
    public Resource csvResource() {
        return new FileSystemResource("src\\main\\resources\\batch-input\\transactions.csv");
    }

    @Bean("transactionsXMLResource")
    public Resource xmlResource() {
        return new FileSystemResource("src\\main\\resources\\batch-input\\transactions.xml");
    }

}
