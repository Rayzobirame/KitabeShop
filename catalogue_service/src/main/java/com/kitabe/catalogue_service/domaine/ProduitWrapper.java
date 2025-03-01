package com.kitabe.catalogue_service.domaine;

/*ProduitWrapper agit comme un "traducteur" entre ProduitEntite (entité JPA) et Produit (objet métier/DTO).
 C’est une classe utilitaire qui encapsule la logique de conversion.
Pourquoi une classe séparée ? Pour centraliser cette transformation et la réutiliser
 ailleurs (par exemple, dans getProduit pour paginer les produits).*/
class ProduitWrapper {
    static Produit toProduit(ProduitEntite produitEntite) {
        return new Produit(
                produitEntite.getCode(),
                produitEntite.getNom(),
                produitEntite.getDescription(),
                produitEntite.getImages(),
                produitEntite.getPrix());
    }
}
