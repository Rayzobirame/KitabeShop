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
    CreerCommandeResponse creerCommande(@RequestBody CreerCommandeRequest creerCommandeRequest){
        if (creerCommandeRequest == null) {
            log.error("creerCommandeRequest est null !");
            throw new IllegalArgumentException("Request body is null");
        }
        if (creerCommandeRequest.items() == null) {
            log.error("Items est null dans creerCommandeRequest !");
        } else {
            log.info("Items reçu : {}", creerCommandeRequest.items());
        }
        String username = securityService.getLoginUsername();
        log.info("creerCommande pour l'utilisateur :" +username);
        return commandeService.creerCommande(username,creerCommandeRequest);
    }
}
