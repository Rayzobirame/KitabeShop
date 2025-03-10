package com.kitabe.commande_service.web.controlleurs;

import com.kitabe.commande_service.domaine.CommandeService;
import com.kitabe.commande_service.domaine.SecurityService;
import com.kitabe.commande_service.domaine.model.CreerCommandeRequest;
import com.kitabe.commande_service.domaine.model.CreerCommandeResponse;

import jakarta.validation.Valid;
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
    CreerCommandeResponse creerCommande(@Valid @RequestBody CreerCommandeRequest creerCommandeRequest){
        String username = securityService.getLoginUsername();
        log.info("creerCommande pour l'utilisateur :" +username);
        if (username == null) {
            username = "visiteur"; // ou une autre valeur par défaut pour les tests
        }
        return commandeService.creerCommande(username,creerCommandeRequest);
    }
}
