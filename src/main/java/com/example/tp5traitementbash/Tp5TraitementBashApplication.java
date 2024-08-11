package com.example.tp5traitementbash;

import com.example.tp5traitementbash.start.BatchLauncher;
import com.example.tp5traitementbash.start.DataSetup;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Tp5TraitementBashApplication {

    public static void main(String[] args) {
        SpringApplication.run(Tp5TraitementBashApplication.class, args);
    }

    @Bean
    CommandLineRunner run(DataSetup dataSetup, BatchLauncher batchLauncher) {
        return args -> {
            // ! populate db(uncomment if db is empty)
            dataSetup.setup();

            // launch batch
            batchLauncher.launchBatchJob();
        };
    }
}
