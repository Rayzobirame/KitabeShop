package com.kitabe.commande_service.domaine;

import com.kitabe.commande_service.ApplicationProperties;
import com.kitabe.commande_service.domaine.model.CommandeAnnuleeEvenement;
import com.kitabe.commande_service.domaine.model.CommandeDelivrerEvenement;
import com.kitabe.commande_service.domaine.model.CommandeErreurEvenement;
import com.kitabe.commande_service.domaine.model.CreerCommandeEvenement;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publie des événements liés aux commandes vers un échange RabbitMQ.
 * Cette classe est responsable de l'envoi des messages (événements) à une file d'attente via RabbitMQ,
 * en utilisant les configurations définies dans {@link ApplicationProperties}.
 */
@Component
public class CommandeEvenementPublisher {
    private RabbitTemplate rabbitTemplate;
    private ApplicationProperties properties;

    /**
     * Construit une instance de {@link CommandeEvenementPublisher}.
     *
     * @param rabbitTemplate L'instance de {@link RabbitTemplate} utilisée pour envoyer des messages à RabbitMQ.
     * @param properties     Les propriétés de l'application contenant les configurations de l'échange et de la file d'attente.
     */
    public CommandeEvenementPublisher(RabbitTemplate rabbitTemplate, ApplicationProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    /**
     * Publie un événement de création de commande dans RabbitMQ.
     * Cette méthode envoie l'événement à la file d'attente configurée pour les nouvelles commandes.
     *
     * @param evenement L'événement de création de commande ({@link CreerCommandeEvenement}) à publier.
     */
    public void publish(CreerCommandeEvenement evenement) {
        this.send(properties.nouvelleCommandeQueue(), evenement);
    }

    public void publish(CommandeDelivrerEvenement evenement) {
        this.send(properties.delivranceCommandeQueue(), evenement);
    }

    public void publish(CommandeAnnuleeEvenement evenement) {
        this.send(properties.annulationCommandeQueue(), evenement);
    }

    public void publish(CommandeErreurEvenement evenement) {
        this.send(properties.erreurCommandeQueue(), evenement);
    }
    /**
     * Envoie un message à RabbitMQ avec une clé de routage et une charge utile (payload).
     * Cette méthode utilise l'échange configuré dans {@link ApplicationProperties} pour router le message.
     *
     * @param routingKey La clé de routage utilisée pour diriger le message vers la bonne file d'attente.
     * @param payload    La charge utile (objet) à envoyer, qui sera sérialisée et envoyée via RabbitMQ.
     */
    private void send(String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(properties.commandeEvenementEchange(), routingKey, payload);
    }
}
