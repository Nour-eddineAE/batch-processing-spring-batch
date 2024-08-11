package com.example.tp5traitementbash.start;

import com.example.tp5traitementbash.entities.Account;
import com.example.tp5traitementbash.services.comptes.AbsAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("data-setup")
@RequiredArgsConstructor
public class DataSetup {
    private final AbsAccountService accountService;

    public void setup() {
        for (int i = 1; i <= 24; i++) {
            Account account = new Account();
            account.setBalance(20000); // Set initial balance
            account.setDebitLimit(-0); // Set withdrawal limit (negative value)

            this.accountService.save(account);
        }
    }
}
