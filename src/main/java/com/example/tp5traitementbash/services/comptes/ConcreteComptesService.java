package com.example.tp5traitementbash.services.comptes;

import com.example.tp5traitementbash.entities.Compte;
import com.example.tp5traitementbash.repositories.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConcreteComptesService implements AbsComptesService {
    @Autowired
    private CompteRepository compteRepository;
    @Override
    public Compte getCompte(Long idCompte) {

        return compteRepository.findById(idCompte)
                .orElseThrow(() -> new RuntimeException("Compte avec id '" + idCompte +  "' non trouvé"));
    }

    @Override
    public void debiterCompte(Compte compte, double montant) {
        compte.debiter(montant);
        compteRepository.save(compte);

    }

    @Override
    public Compte save(Compte compte) {
        return compteRepository.save(compte);
    }

}
