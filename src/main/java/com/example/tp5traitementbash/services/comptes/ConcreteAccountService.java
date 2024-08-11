package com.example.tp5traitementbash.services.comptes;

import com.example.tp5traitementbash.entities.Account;
import com.example.tp5traitementbash.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConcreteAccountService implements AbsAccountService {
    private final AccountRepository accountRepository;
    @Override
    public Account getAccount(Long accountId) {

        return this.accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account with id '" + accountId +  "' not found!"));
    }

    @Override
    public Account save(Account account) {
        return this.accountRepository.save(account);
    }

}
