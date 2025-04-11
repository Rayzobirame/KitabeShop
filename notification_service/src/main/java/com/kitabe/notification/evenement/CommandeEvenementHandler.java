package com.kitabe.notification.evenement;

import com.kitabe.notification.domaine.NotificationServices;
import com.kitabe.notification.domaine.model.CommandeAnnuleeEvenement;
import com.kitabe.notification.domaine.model.CommandeDelivrerEvenement;
import com.kitabe.notification.domaine.model.CommandeErreurEvenement;
import com.kitabe.notification.domaine.model.CreerCommandeEvenement;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Gestionnaire des événements de commande pour le service de notification.
 * Cette classe écoute les messages RabbitMQ provenant de différentes files d'attente
 * et déclenche l'envoi de notifications par email via NotificationServices.
 * Les événements traités incluent la création, la livraison, l'annulation et les erreurs de commande.
 */
@Component
public class CommandeEvenementHandler {
    private NotificationServices notificationServices;

    /**
     * Constructeur du gestionnaire d'événements de commande.
     *
     * @param notificationServices Service de notification utilisé pour envoyer des emails.
     */
    public CommandeEvenementHandler(NotificationServices notificationServices) {
        this.notificationServices = notificationServices;
    }

    /**
     * Traite un événement de création de commande reçu via RabbitMQ.
     * Déclenche l'envoi d'une notification par email au client pour confirmer la création de la commande.
     *
     * @param evenement Événement de création de commande contenant les informations du client et de la commande.
     */
    @RabbitListener(queues = "${notification.nouvelle-commande-queue}")
    void handleCreerCommandeEvenement(CreerCommandeEvenement evenement){
        System.out.println(" Cette evenement nommé :"+evenement +"vient d'etre creer");
        notificationServices.sendCommandeCreerNotification(evenement);
    }

    /**
     * Traite un événement de livraison de commande reçu via RabbitMQ.
     * Déclenche l'envoi d'une notification par email au client pour confirmer la livraison de la commande.
     *
     * @param evenement Événement de livraison de commande contenant les informations du client et de la commande.
     */
    @RabbitListener(queues = "${notification.delivrance-commande-queue}")
    void handleDelivrerCommandeEvenement(CommandeDelivrerEvenement evenement){
        System.out.println(" Cette evenement nommé :"+evenement +"vient d'etre livrer avec succees");
        notificationServices.sendCommandeLivrerNotification(evenement);
    }

    /**
     * Traite un événement d'annulation de commande reçu via RabbitMQ.
     * Déclenche l'envoi d'une notification par email au client pour l'informer de l'annulation.
     *
     * @param evenement Événement d'annulation de commande contenant les informations du client, de la commande et de la raison.
     */
    @RabbitListener(queues = "${notification.annulation-commande-queue}")
    void handleAnnulerCommandeEvenement(CommandeAnnuleeEvenement evenement){
        System.out.println(" Cette evenement nommé :"+evenement +"vient d'etre annuler");
        notificationServices.sendCommandeAnnulerNotification(evenement);
    }

    /**
     * Traite un événement d'erreur de commande reçu via RabbitMQ.
     * Déclenche l'envoi d'une notification par email à l'équipe pour signaler l'échec de la commande.
     *
     * @param evenement Événement d'erreur de commande contenant les détails de l'erreur et de la commande.
     */
    @RabbitListener(queues = "${notification.erreur-commande-queue}")
    void handleErreurCommandeEvenement(CommandeErreurEvenement evenement){
        System.out.println(" Cette evenement nommé :"+evenement +"vient de generer une erreur");
        notificationServices.sendCommandeErreurNotification(evenement);
    }

}
