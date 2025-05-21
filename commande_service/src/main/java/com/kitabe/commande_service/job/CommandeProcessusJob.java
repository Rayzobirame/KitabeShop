package com.kitabe.commande_service.job;

import com.kitabe.commande_service.domaine.CommandeService;
import java.time.Instant;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Job planifié pour traiter les nouvelles commandes.
 * Cette classe est responsable de déclencher périodiquement le traitement des commandes ayant le statut NOUVEAU
 * via le service {@link CommandeService}. La fréquence d'exécution est définie par une expression cron
 * dans les propriétés de l'application. ShedLock est utilisé pour garantir qu'une seule instance exécute cette tâche
 * dans un environnement distribué.
 */
@Component
public class CommandeProcessusJob {

    private static final Logger log = LoggerFactory.getLogger(CommandeProcessusJob.class);
    private final CommandeService commandeService;

    public CommandeProcessusJob(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    /**
     * Traite les nouvelles commandes selon un calendrier défini par l'expression cron.
     * Cette méthode est exécutée périodiquement (selon la propriété {@code commande.nouveau-commande-Job-cron})
     * et appelle le service {@link CommandeService} pour traiter les commandes ayant le statut NOUVEAU.
     * ShedLock garantit que cette méthode ne s'exécute que sur une seule instance à la fois dans un environnement distribué.
     * La vérification {@link LockAssert#assertLocked()} assure que le verrou a été acquis avant l'exécution.
     */
    @Scheduled(cron = "${commande.nouveau-commande-Job-cron}")
    @SchedulerLock(name = "nouveauCommandeEvenement")
    void nouveauCommandeEvenement() {
        LockAssert.assertLocked();
        log.info("Publication de commandeEvenement a {}", Instant.now());
        commandeService.nouveauProcessusCommande();
    }
}
