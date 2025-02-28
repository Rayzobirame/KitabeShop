package com.kitabe.catalogue_service.domaine;

public class ProduitNotFoundException extends RuntimeException{
    public ProduitNotFoundException(String message){
        super(message);
    }

    public static ProduitNotFoundException forCode (String code){
        return new ProduitNotFoundException("Le produit avec le code "+code +" n'existe pas !");
    }
}
