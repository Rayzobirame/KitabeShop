package com.kitabe.kitabe_webapp.Controllers;

import com.kitabe.kitabe_webapp.catalogue.Produit;
import com.kitabe.kitabe_webapp.commande.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class CommandeController {

    private final CommandeServiceClient commandeServiceClient;

    public CommandeController(CommandeServiceClient commandeServiceClient) {
        this.commandeServiceClient = commandeServiceClient;
    }

    @GetMapping("/panier")
    public String panier(){
        return "panier";
    }

    @PostMapping("/api/commande")
    @ResponseBody
    CommandeConfirmationDTO creerCommande(@Valid @RequestBody CreerCommandeRequest request, @RequestHeader Map<String, ?> headers){
        return commandeServiceClient.creerCommande(headers, request);
    }

    @GetMapping("/api/commande/{commandeNum}")
    @ResponseBody
    CommandeDTO getCommande(@RequestHeader(required = false) Map<String, ?> headers, @PathVariable String commandeNum) {
        return commandeServiceClient.getCommande(headers, commandeNum);
    }

    @GetMapping("/commandes/{commandeNum}")
    String showCommandeDetails(@PathVariable String commandeNum, Model model){
        model.addAttribute("commandeNum", commandeNum);
        return "commande_details";
    }

    @GetMapping("/api/commande")
    @ResponseBody
    List<CommandeSommaire> getCommandes(@RequestHeader(required = false) Map<String, ?> headers) {
        return commandeServiceClient.getCommandes(headers);
    }

    @GetMapping("/commandes")
    String showCommande(){
        return "commandes";
    }

}
