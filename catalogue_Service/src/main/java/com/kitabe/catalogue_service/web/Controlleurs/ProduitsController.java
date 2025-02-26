package com.kitabe.catalogue_service.web.Controlleurs;

import com.kitabe.catalogue_service.domaine.PagedResult;
import com.kitabe.catalogue_service.domaine.Produit;
import com.kitabe.catalogue_service.domaine.ProduitServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
class ProduitsController {

    private final ProduitServices produitServices;

    ProduitsController(ProduitServices produitServices) {
        this.produitServices = produitServices;
    }

    @GetMapping
    PagedResult<Produit> getProduits(@RequestParam(name = "page", defaultValue = "1") int pageNum ){
        return produitServices.getProduit(pageNum);
    }
}
