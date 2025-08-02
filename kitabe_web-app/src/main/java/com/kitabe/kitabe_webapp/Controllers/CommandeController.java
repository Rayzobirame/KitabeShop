package com.kitabe.kitabe_webapp.Controllers;

import com.kitabe.kitabe_webapp.commande.*;
import com.kitabe.kitabe_webapp.service.SecurityHelper;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommandeController {

    private final CommandeServiceClient commandeServiceClient;
    private final SecurityHelper helper;

    public CommandeController(CommandeServiceClient commandeServiceClient, SecurityHelper helper) {
        this.commandeServiceClient = commandeServiceClient;
        this.helper = helper;
    }

    @GetMapping("/panier")
    public String panier() {
        return "panier";
    }

    @PostMapping("/api/commandes")
    @ResponseBody
    CommandeConfirmationDTO creerCommande(@Valid @RequestBody CreerCommandeRequest request) {
        return commandeServiceClient.creerCommande(getHeaders(), request);
    }

    @GetMapping("/api/commandes/{commandeNum}")
    @ResponseBody
    CommandeDTO getCommande( @PathVariable String commandeNum) {
        return commandeServiceClient.getCommande(getHeaders(), commandeNum);
    }

    @GetMapping("/commandes/{commandeNum}")
    String showCommandeDetails(@PathVariable String commandeNum, Model model) {
        model.addAttribute("commandeNum", commandeNum);
        return "commande_details";
    }

    @GetMapping("/api/commandes")
    @ResponseBody
    List<CommandeSommaire> getCommandes() {
        return commandeServiceClient.getCommandes(getHeaders());
    }

    @GetMapping("/commandes")
    String showCommande() {
        return "commandes";
    }

    private Map<String, ?> getHeaders() {
        String accessToken = helper.getAccessToken();
        return Map.of("Authorization", "Bearer " + accessToken);
    }

}
