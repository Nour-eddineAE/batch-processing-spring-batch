package com.example.tp5traitementbash.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    private Long transactionId;
    private double amount;
    private Date debitDate;
    private Date transactionDate;
    @ManyToOne(cascade = CascadeType.ALL)
    private Account account;
}
