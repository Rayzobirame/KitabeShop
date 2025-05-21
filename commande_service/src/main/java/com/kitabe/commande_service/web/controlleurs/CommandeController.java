package com.kitabe.commande_service.web.controlleurs;

import com.kitabe.commande_service.domaine.CommandeNotFoundException;
import com.kitabe.commande_service.domaine.CommandeService;
import com.kitabe.commande_service.domaine.SecurityService;
import com.kitabe.commande_service.domaine.model.CommandeDTO;
import com.kitabe.commande_service.domaine.model.CommandeSommaire;
import com.kitabe.commande_service.domaine.model.CreerCommandeRequest;
import com.kitabe.commande_service.domaine.model.CreerCommandeResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commande")
public class CommandeController {
    private final CommandeService commandeService;
    private final SecurityService securityService;
    private static final Logger log = LoggerFactory.getLogger(CommandeController.class);

    public CommandeController(CommandeService commandeService, SecurityService securityService) {
        this.commandeService = commandeService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreerCommandeResponse creerCommande(@Valid @RequestBody CreerCommandeRequest creerCommandeRequest) {
        String username = securityService.getLoginUsername();
        log.info("creerCommande pour l'utilisateur :" + username);
        if (username == null) {
            username = "visiteur"; // ou une autre valeur par défaut pour les tests
        }
        return commandeService.creerCommande(username, creerCommandeRequest);
    }

    /**
     * Récupère un sommaire de toutes les commandes d'un utilisateur authentifié.
     * Cette méthode utilise le service de sécurité pour obtenir le nom d'utilisateur de l'utilisateur connecté,
     * puis récupère un sommaire de ses commandes via le service {@link CommandeService}.
     *
     * @return Une liste de sommaires de commandes ({@link CommandeSommaire}) pour l'utilisateur connecté.
     */
    @GetMapping
    List<CommandeSommaire> getCommandes() {
        String pseudo = securityService.getLoginUsername();
        log.info("creerCommande pour l'utilisateur :" + pseudo);
        return commandeService.trouveCommande(pseudo);
    }
    /**
     * Récupère les détails d'une commande spécifique pour un utilisateur authentifié.
     * Cette méthode utilise le service de sécurité pour obtenir le nom d'utilisateur de l'utilisateur connecté,
     * puis récupère les détails de la commande spécifiée par son numéro via le service {@link CommandeService}.
     *
     * @param commandeNum Le numéro unique de la commande à récupérer.
     * @return Les détails de la commande sous forme de {@link CommandeDTO}.
     * @throws CommandeNotFoundException Si aucune commande n'est trouvée pour le numéro spécifié.
     */
    @GetMapping(value = "/{commandeNum}")
    CommandeDTO getCommande(@PathVariable(value = "commandeNum") String commandeNum) {
        log.info("recuperer la commande a base de l'id :" + commandeNum);
        String username = securityService.getLoginUsername();
        return commandeService
                .trouveCommandeClient(username, commandeNum)
                .orElseThrow(() -> new CommandeNotFoundException(commandeNum));
    }
}
