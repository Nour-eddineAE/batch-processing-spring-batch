package com.example.tp5traitementbash.services.comptes;

import com.example.tp5traitementbash.entities.Compte;

public interface AbsComptesService {
    Compte getCompte(Long idCompte);
    void debiterCompte(Compte compte, double montant);
    Compte save(Compte compte);
}
