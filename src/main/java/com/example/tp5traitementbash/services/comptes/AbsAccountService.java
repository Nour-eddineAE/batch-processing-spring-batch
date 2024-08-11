package com.example.tp5traitementbash.services.comptes;

import com.example.tp5traitementbash.entities.Account;

public interface AbsAccountService {
    Account getAccount(Long idCompte);
    Account save(Account compte);
}
