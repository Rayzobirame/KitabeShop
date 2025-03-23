package com.kitabe.commande_service.domaine;

import com.kitabe.commande_service.domaine.model.CommandeMapper;
import com.kitabe.commande_service.domaine.model.CreerCommandeEvenement;
import com.kitabe.commande_service.domaine.model.CreerCommandeRequest;
import com.kitabe.commande_service.domaine.model.CreerCommandeResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommandeService {
    private final CommandeRepository commandeRepository;
    private CommandeValidateur validateur;
    private CommandeEvenementService evenementService;
    private static final Logger log = LoggerFactory.getLogger(CommandeService.class);

    public CommandeService(CommandeRepository commandeRepository, CommandeValidateur validateur, CommandeEvenementService evenementService) {
        this.commandeRepository = commandeRepository;
        this.validateur = validateur;
        this.evenementService = evenementService;
    }


    public CreerCommandeResponse creerCommande(String username, CreerCommandeRequest createCommandeRequest) {
        validateur.validateur(createCommandeRequest);
        CommandeEntite nouvelleCommande = CommandeMapper.convertToEntite(createCommandeRequest);
        nouvelleCommande.setPseudo(username);
        CommandeEntite savedCommande = this.commandeRepository.save(nouvelleCommande);
        log.info("Une nouvelle commande a été creer avec : " + savedCommande.getCommandeNum());
        CreerCommandeEvenement commandeCreerEvenement = CommandeEvenementMapper.buildCommandeCreerSurEvenement(savedCommande);
        evenementService.save(commandeCreerEvenement);
        return new CreerCommandeResponse(savedCommande.getCommandeNum());
    }
}
