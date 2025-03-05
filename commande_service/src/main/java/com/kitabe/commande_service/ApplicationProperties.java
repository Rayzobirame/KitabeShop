package com.kitabe.commande_service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "commande")
public record ApplicationProperties(
        String commandeEvenementEchange,
        String nouvelleCommandeQueue,
        String delivranceCommandeQueue,
        String annulationCommandeQueue,
        String erreurCommandeQueue
){

}

