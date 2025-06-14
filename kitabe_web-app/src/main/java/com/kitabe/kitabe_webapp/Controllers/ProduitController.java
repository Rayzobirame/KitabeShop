package com.kitabe.kitabe_webapp.Controllers;

import com.kitabe.kitabe_webapp.catalogue.CatalogueServiceClient;
import com.kitabe.kitabe_webapp.catalogue.PagedResult;
import com.kitabe.kitabe_webapp.catalogue.Produit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProduitController {

    private CatalogueServiceClient catalogueServiceClient;

    public ProduitController(CatalogueServiceClient catalogueServiceClient) {
        this.catalogueServiceClient = catalogueServiceClient;
    }

    @GetMapping
    String index(){
        return "redirect:/produits";
    }

    @GetMapping("/produits")
    String produitPage(@RequestParam(name="page", defaultValue="1")int pageNum, Model model){
        model.addAttribute("pageNum", pageNum);
        return "produits";
    }

    @GetMapping("/api/produits")
    @ResponseBody
    PagedResult<Produit> produits(@RequestParam(name="page", defaultValue="1")int page, Model model){
        model.addAttribute("pageNum", page);
        return catalogueServiceClient.getProduits(page);
    }


}
