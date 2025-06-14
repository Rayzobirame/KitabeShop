package com.kitabe.kitabe_webapp.catalogue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface CatalogueServiceClient {

    @GetExchange("/catalogue/api/produits")
    PagedResult<Produit>getProduits(@RequestParam int page);

    @GetExchange("/catalogue/api/produits/{code}")
    ResponseEntity <Produit>getProduitByCode(@PathVariable String code);

}
