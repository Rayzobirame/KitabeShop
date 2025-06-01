package com.kitabe.kitabe_webapp.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CommandeController {

    @GetMapping("/panier")
    public String panier(){
        return "panier";
    }

    @GetMapping("/commandes/{commandeNum}")
    String showCommandeDetails(@PathVariable String commandeNum, Model model){
        model.addAttribute("commandeNum", commandeNum);
        return "commande_details";
    }
}
