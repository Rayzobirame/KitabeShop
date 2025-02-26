package com.kitabe.catalogue_service.domaine;

class ProduitWrapper {
    static Produit toProduit(ProduitEntite produitEntite) {
        return new Produit(
                produitEntite.getCode(),
                produitEntite.getNom(),
                produitEntite.getDescription(),
                produitEntite.getImages(),
                produitEntite.getPrix()
        );
    }
}
