package com.example.tp5traitementbash.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCompte;
    private double solde;

    // donner la possibilité de debiter un compte jusqu'à une limite(doit étre une valeur négative)
    private double limite;

    @OneToMany(mappedBy = "compte")
    private List<Transaction> transactions;

    public void debiter(double montant) {
        if(solde  - montant < limite)
            throw new RuntimeException("Valeur limite de débit est dépassée");

        solde -= montant;
    }
}
