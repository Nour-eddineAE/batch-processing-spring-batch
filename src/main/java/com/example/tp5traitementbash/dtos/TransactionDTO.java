package com.example.tp5traitementbash.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long idTransaction;
    private double montant;
    private Date dateTransaction;
    private Long idCompte;
}
