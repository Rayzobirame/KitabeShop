package com.kitabe.kitabe_webapp.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProduitController {

    @GetMapping
    String index(){
        return "redirect:/produits";
    }

    @GetMapping("/produits")
    String produitPage(@RequestParam(name="page", defaultValue="1")int pageNum, Model model){
        model.addAttribute("pageNum", pageNum);
        return "produits";
    }
}
