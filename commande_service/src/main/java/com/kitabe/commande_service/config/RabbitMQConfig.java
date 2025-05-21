package com.kitabe.commande_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitabe.commande_service.ApplicationProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private final ApplicationProperties properties;

    public RabbitMQConfig(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(properties.commandeEvenementEchange());
    }

    // Déclaration des files d'attente
    @Bean
    public Queue nouvelleCommandeQueue() {
        return QueueBuilder.durable(properties.nouvelleCommandeQueue()).build(); // Durable
    }

    @Bean
    public Queue delivranceCommandeQueue() {
        return QueueBuilder.durable(properties.delivranceCommandeQueue()).build(); // Durable
    }

    @Bean
    public Queue annulationCommandeQueue() {
        return QueueBuilder.durable(properties.annulationCommandeQueue()).build(); // Durable
    }

    @Bean
    public Queue erreurCommandeQueue() {
        return QueueBuilder.durable(properties.erreurCommandeQueue()).build(); // Durable
    }

    // Déclaration des bindings (liaisons entre exchange et queues)
    @Bean
    Binding nouvelleCommandeBinding() {
        return BindingBuilder.bind(nouvelleCommandeQueue())
                .to(exchange())
                .with(properties.nouvelleCommandeQueue()); // Routing key pour nouvelle commande
    }

    @Bean
    public Binding delivranceCommandeBinding() {
        return BindingBuilder.bind(delivranceCommandeQueue())
                .to(exchange())
                .with(properties.delivranceCommandeQueue()); // Routing key pour livraison
    }

    @Bean
    public Binding annulationCommandeBinding() {
        return BindingBuilder.bind(annulationCommandeQueue())
                .to(exchange())
                .with(properties.annulationCommandeQueue()); // Routing key pour annulation
    }

    @Bean
    public Binding erreurCommandeBinding() {
        return BindingBuilder.bind(erreurCommandeQueue())
                .to(exchange())
                .with(properties.erreurCommandeQueue()); // Routing key pour erreur
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverte(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
