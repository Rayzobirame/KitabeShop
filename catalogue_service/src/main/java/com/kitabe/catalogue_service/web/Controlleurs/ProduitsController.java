package com.kitabe.catalogue_service.web.Controlleurs;

import com.kitabe.catalogue_service.domaine.PagedResult;
import com.kitabe.catalogue_service.domaine.Produit;
import com.kitabe.catalogue_service.domaine.ProduitNotFoundException;
import com.kitabe.catalogue_service.domaine.ProduitServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produits")
class ProduitsController {

    private final ProduitServices produitServices;

    /**
     * Constructeur pour l'injection du service ProduitServices.
     * @param produitServices Service pour les opérations sur les produits.
     */
    ProduitsController(ProduitServices produitServices) {
        this.produitServices = produitServices;
    }

    /**
     * Récupère une liste paginée de produits via une requête GET.
     * @param pageNum Numéro de page (défaut 1 si non spécifié).
     * @return PagedResult avec les produits et métadonnées de pagination.
     */
    @GetMapping
    PagedResult<Produit> getProduits(@RequestParam(name = "page", defaultValue = "1") int pageNum) {
        return produitServices.getProduit(pageNum); // Délègue au service
    }

    @GetMapping("/{code}")
    ResponseEntity<Produit> getProduitByCode(@PathVariable String code) {
        return produitServices
                .getProduitByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProduitNotFoundException.forCode(code));
    }

}
