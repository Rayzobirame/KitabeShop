package com.kitabe.notification;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notification")
public record ApplicationProperties(
        String supportEmail,
        String commandeEvenementEchange,
        String nouvelleCommandeQueue,
        String delivranceCommandeQueue,
        String annulationCommandeQueue,
        String erreurCommandeQueue
){

}

