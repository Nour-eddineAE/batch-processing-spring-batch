package com.example.tp5traitementbash;

import com.example.tp5traitementbash.batch.BatchLauncher;
import com.example.tp5traitementbash.entities.Compte;
import com.example.tp5traitementbash.services.comptes.AbsComptesService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@SpringBootApplication
public class Tp5TraitementBashApplication {

    public static void main(String[] args) {
        SpringApplication.run(Tp5TraitementBashApplication.class, args);
    }

    @Bean
    CommandLineRunner run(AbsComptesService comptesService, BatchLauncher batchLauncher) {
        return args -> {
            String filePath = "src\\main\\resources\\transactions.csv";

            try (FileWriter writer = new FileWriter(filePath)) {
                // Write the CSV header
                writer.write("idTransaction,idCompte,montant,dateTransaction\n");

                // Generate 20 transactions
                for (int i = 1; i <= 20; i++) {
                    Compte compte = new Compte();
                    compte.setSolde(5000); // Set initial balance
                    compte.setLimite(-1000); // Set withdrawal limit (negative value)

                    comptesService.save(compte);

                    // Generate a random amount for the transaction
                    double montant = Math.random() * 5000;

                    // Generate a random date for the transaction
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateTransaction = dateFormat.format(new Date());;

                    // Write the transaction data to the CSV file
                    writer.write(compte.getIdCompte() + "," + compte.getIdCompte() + "," + montant + "," + dateTransaction + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // launch batch
            batchLauncher.launchBatchJob();
        };
    }

    ;
}
