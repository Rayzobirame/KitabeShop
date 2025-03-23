package com.kitabe.commande_service.job;

import com.kitabe.commande_service.domaine.CommandeEvenementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
/**
 * Job planifié pour publier les événements de commande.
 * Cette classe est responsable de déclencher périodiquement la publication des événements de commande
 * via le service {@link CommandeEvenementService}. La fréquence d'exécution est définie par une expression cron
 * dans les propriétés de l'application.
 */
@Component
public class CommandeEvenenementPublishingJob {
    private static final Logger log = LoggerFactory.getLogger(CommandeEvenenementPublishingJob.class);
    private CommandeEvenementService commandeEvenementService;

    /**
     * Construit une instance de {@link CommandeEvenenementPublishingJob}.
     *
     * @param commandeEvenementService Le service utilisé pour publier les événements de commande.
     */
    public CommandeEvenenementPublishingJob(CommandeEvenementService commandeEvenementService) {
        this.commandeEvenementService = commandeEvenementService;
    }

    /**
     * Publie les événements de commande selon un calendrier défini par l'expression cron.
     * Cette méthode est exécutée périodiquement (selon la propriété {@code commande.publish-commande-evenement-Job-cron})
     * et appelle le service {@link CommandeEvenementService} pour publier les événements en attente.
     */
    @Scheduled(cron = "${commande.publish-commande-evenement-Job-cron}")
    void publishCommandeEvenement() {
        log.info("Publication de commandeEvenement a {}", Instant.now());
        commandeEvenementService.publishCommandeOrder();
    }
}
