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
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    private double balance;

    // give ability to debit account under the limit(can be a negative value)
    private double debitLimit;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

    public void debit(double ammount) {
        if(this.balance  - ammount < this.debitLimit)
            throw new RuntimeException("debit limit was exceeded: \t account: " + this.accountId +
                    "\t balance: " + this.balance +
                    "\t ammount: " + ammount +
                    "\t limit: " + this.debitLimit);

        this.balance -= ammount;
    }
}
