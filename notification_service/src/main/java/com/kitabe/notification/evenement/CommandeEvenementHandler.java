package com.kitabe.notification.evenement;

import com.kitabe.notification.domaine.CommandeEvenementEntite;
import com.kitabe.notification.domaine.CommandeEvenementRepository;
import com.kitabe.notification.domaine.NotificationServices;
import com.kitabe.notification.domaine.model.CommandeAnnuleeEvenement;
import com.kitabe.notification.domaine.model.CommandeDelivrerEvenement;
import com.kitabe.notification.domaine.model.CommandeErreurEvenement;
import com.kitabe.notification.domaine.model.CreerCommandeEvenement;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Gestionnaire des événements de commande pour le service de notification.
 * Cette classe écoute les messages RabbitMQ provenant de différentes files d'attente
 * et déclenche l'envoi de notifications par email via NotificationServices.
 * Les événements traités incluent la création, la livraison, l'annulation et les erreurs de commande.
 * Elle vérifie également les duplications d'événements et persiste les événements traités dans la base de données.
 */
@Component
public class CommandeEvenementHandler {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CommandeEvenementHandler.class);
    private NotificationServices notificationServices;
    private CommandeEvenementRepository commandeEvenementRepository;

    /**
     * Constructeur du gestionnaire d'événements de commande.
     *
     * @param notificationServices Service de notification utilisé pour envoyer des emails.
     */
    public CommandeEvenementHandler(NotificationServices notificationServices,CommandeEvenementRepository commandeEvenementRepository) {
        this.notificationServices = notificationServices;
        this.commandeEvenementRepository = commandeEvenementRepository;
    }

    /**
     * Traite un événement de création de commande reçu via RabbitMQ.
     * Vérifie si l'événement est une duplication, puis déclenche l'envoi d'une notification par email
     * au client pour confirmer la création de la commande. Persiste l'événement dans la base de données.
     *
     * @param evenement Événement de création de commande contenant les informations du client et de la commande.
     */
    @RabbitListener(queues = "${notification.nouvelle-commande-queue}")
    void handleCreerCommandeEvenement(CreerCommandeEvenement evenement){
        log.info(" Cette evenement nommé :"+evenement +"vient d'etre creer");
        if(commandeEvenementRepository.existsByEvenementId(evenement.evenementId())){
            log.warn("Nous avons detecté une duplication lors de la creation devenement commande pour ID"+evenement.evenementId());
            return;
        }
        notificationServices.sendCommandeCreerNotification(evenement);
        CommandeEvenementEntite commandeEvenement = new CommandeEvenementEntite(evenement.evenementId());
        commandeEvenementRepository.save(commandeEvenement);
        log.info("Événement de création de commande traité et persisté : evenementId={}", evenement.evenementId());
    }

    /**
     * Traite un événement de livraison de commande reçu via RabbitMQ.
     * Vérifie si l'événement est une duplication, puis déclenche l'envoi d'une notification par email
     * au client pour confirmer la livraison de la commande. Persiste l'événement dans la base de données.
     *
     * @param evenement Événement de livraison de commande contenant les informations du client et de la commande.
     */
    @RabbitListener(queues = "${notification.delivrance-commande-queue}")
    void handleDelivrerCommandeEvenement(CommandeDelivrerEvenement evenement){
        log.info("Réception d'un événement de livraison de commande : commandeNum={}, evenementId={}",
                evenement.commandeNum(), evenement.evenementId());
        if(commandeEvenementRepository.existsByEvenementId(evenement.evenementId())){
            log.warn("Nous avons detecté une duplication lors de la livraison devenement commande pour ID"+evenement.evenementId());
            return;
        }
        notificationServices.sendCommandeLivrerNotification(evenement);
        CommandeEvenementEntite commandeEvenement = new CommandeEvenementEntite(evenement.evenementId());
        commandeEvenementRepository.save(commandeEvenement);
        log.info("Événement de livraison de commande traité et persisté : evenementId={}", evenement.evenementId());
    }

    /**
     * Traite un événement d'annulation de commande reçu via RabbitMQ.
     * Vérifie si l'événement est une duplication, puis déclenche l'envoi d'une notification par email
     * au client pour l'informer de l'annulation. Persiste l'événement dans la base de données.
     *
     * @param evenement Événement d'annulation de commande contenant les informations du client, de la commande et de la raison.
     */
    @RabbitListener(queues = "${notification.annulation-commande-queue}")
    void handleAnnulerCommandeEvenement(CommandeAnnuleeEvenement evenement){
        log.info("Réception d'un événement d'annulation de commande : commandeNum={}, evenementId={}",
                evenement.commandeNum(), evenement.evenementId());

        if (commandeEvenementRepository.existsByEvenementId(evenement.evenementId())) {
            log.warn("Duplication détectée lors de l'annulation de l'événement de commande pour evenementId={}",
                    evenement.evenementId());
            return;
        }
        notificationServices.sendCommandeAnnulerNotification(evenement);
        CommandeEvenementEntite commandeEvenement = new CommandeEvenementEntite(evenement.evenementId());
        commandeEvenementRepository.save(commandeEvenement);
        log.info("Événement d'annulation de commande traité et persisté : evenementId={}", evenement.evenementId());
    }

    /**
     * Traite un événement d'erreur de commande reçu via RabbitMQ.
     * Vérifie si l'événement est une duplication, puis déclenche l'envoi d'une notification par email
     * à l'équipe pour signaler l'échec de la commande. Persiste l'événement dans la base de données.
     *
     * @param evenement Événement d'erreur de commande contenant les détails de l'erreur et de la commande.
     */
    @RabbitListener(queues = "${notification.erreur-commande-queue}")
    void handleErreurCommandeEvenement(CommandeErreurEvenement evenement){
        log.info("Réception d'un événement d'erreur de commande : commandeNum={}, evenementId={}",
                evenement.commandeNum(), evenement.evenementId());
        if (commandeEvenementRepository.existsByEvenementId(evenement.evenementId())) {
            log.warn("Duplication détectée lors du traitement de l'erreur de l'événement de commande pour evenementId={}",
                    evenement.evenementId());
            return;
        }
        notificationServices.sendCommandeErreurNotification(evenement);
        CommandeEvenementEntite commandeEvenement = new CommandeEvenementEntite(evenement.evenementId());
        commandeEvenementRepository.save(commandeEvenement);
        log.info("Événement d'erreur de commande traité et persisté : evenementId={}", evenement.evenementId());
    }

}
