package com.kitabe.notification.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitabe.notification.ApplicationProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration RabbitMQ pour le service de notification.
 * Cette classe définit les beans nécessaires pour configurer les files d'attente,
 * l'échange (exchange), les liaisons (bindings), et le convertisseur de messages JSON.
 * Les noms des files et de l'échange sont récupérés à partir des propriétés de l'application.
 */
@Configuration
public class RabbitMQConfig {

    private final ApplicationProperties properties;

    public RabbitMQConfig(ApplicationProperties properties) {
        this.properties = properties;
    }

    /**
     * Crée un échange de type DirectExchange pour les événements de commande.
     *
     * @return DirectExchange configuré avec le nom spécifié dans les propriétés.
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(properties.commandeEvenementEchange());
    }

    /**
     * Crée une file d'attente durable pour les événements de création de commande.
     *
     * @return Queue durable pour les nouvelles commandes.
     */
    @Bean
    public Queue nouvelleCommandeQueue() {
        return QueueBuilder.durable(properties.nouvelleCommandeQueue()).build(); // Durable
    }

    /**
     * Crée une file d'attente durable pour les événements de livraison de commande.
     *
     * @return Queue durable pour les livraisons de commandes.
     */
    @Bean
    public Queue delivranceCommandeQueue() {
        return QueueBuilder.durable(properties.delivranceCommandeQueue()).build(); // Durable
    }

    /**
     * Crée une file d'attente durable pour les événements d'annulation de commande.
     *
     * @return Queue durable pour les annulations de commandes.
     */
    @Bean
    public Queue annulationCommandeQueue() {
        return QueueBuilder.durable(properties.annulationCommandeQueue()).build(); // Durable
    }

    /**
     * Crée une file d'attente durable pour les événements d'erreur de commande.
     *
     * @return Queue durable pour les erreurs de commandes.
     */
    @Bean
    public Queue erreurCommandeQueue() {
        return QueueBuilder.durable(properties.erreurCommandeQueue()).build(); // Durable
    }

    // Déclaration des bindings (liaisons entre exchange et queues)
    /**
     * Crée une liaison entre la file des nouvelles commandes et l'échange.
     * Utilise la clé de routage spécifiée dans les propriétés.
     *
     * @return Binding pour la file des nouvelles commandes.
     */
    @Bean
    Binding nouvelleCommandeBinding() {
        return BindingBuilder.bind(nouvelleCommandeQueue())
                .to(exchange())
                .with(properties.nouvelleCommandeQueue()); // Routing key pour nouvelle commande
    }

    /**
     * Crée une liaison entre la file des livraisons de commandes et l'échange.
     * Utilise la clé de routage spécifiée dans les propriétés.
     *
     * @return Binding pour la file des livraisons de commandes.
     */
    @Bean
    public Binding delivranceCommandeBinding() {
        return BindingBuilder.bind(delivranceCommandeQueue())
                .to(exchange())
                .with(properties.delivranceCommandeQueue()); // Routing key pour livraison
    }

    /**
     * Crée une liaison entre la file des annulations de commandes et l'échange.
     * Utilise la clé de routage spécifiée dans les propriétés.
     *
     * @return Binding pour la file des annulations de commandes.
     */
    @Bean
    public Binding annulationCommandeBinding() {
        return BindingBuilder.bind(annulationCommandeQueue())
                .to(exchange())
                .with(properties.annulationCommandeQueue()); // Routing key pour annulation
    }

    /**
     * Crée une liaison entre la file des erreurs de commandes et l'échange.
     * Utilise la clé de routage spécifiée dans les propriétés.
     *
     * @return Binding pour la file des erreurs de commandes.
     */
    @Bean
    public Binding erreurCommandeBinding() {
        return BindingBuilder.bind(erreurCommandeQueue())
                .to(exchange())
                .with(properties.erreurCommandeQueue()); // Routing key pour erreur
    }

    /**
     * Configure un RabbitTemplate pour envoyer et recevoir des messages RabbitMQ.
     * Utilise un convertisseur JSON pour sérialiser/désérialiser les messages.
     *
     * @param connectionFactory Factory de connexion RabbitMQ.
     * @param objectMapper Mapper Jackson pour la sérialisation/désérialisation JSON.
     * @return RabbitTemplate configuré.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
        return rabbitTemplate;
    }

    /**
     * Crée un convertisseur de messages JSON pour RabbitMQ.
     * Utilise Jackson pour sérialiser/désérialiser les messages en JSON.
     *
     * @param objectMapper Mapper Jackson pour la sérialisation/désérialisation JSON.
     * @return Convertisseur de messages JSON.
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverte(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
