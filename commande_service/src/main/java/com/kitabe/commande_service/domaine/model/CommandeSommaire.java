package com.kitabe.commande_service.domaine.model;

import com.kitabe.commande_service.domaine.CommandeStatus;

public record CommandeSommaire(String commandeNum, CommandeStatus status) {
}
