package com.kitabe.commande_service.domaine.model;

public class InvalideCommandeException extends RuntimeException {
    public InvalideCommandeException(String message) {
        super(message);
    }
}
