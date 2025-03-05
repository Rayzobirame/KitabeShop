package com.kitabe.commande_service.domaine;

public class CommandeNotFoundException extends RuntimeException{
    public CommandeNotFoundException(String message){
        super(message);
    }

}
