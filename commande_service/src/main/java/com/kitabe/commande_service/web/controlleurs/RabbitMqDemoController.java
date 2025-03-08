package com.kitabe.commande_service.web.controlleurs;

import com.kitabe.commande_service.ApplicationProperties;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitMqDemoController {
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties applicationProperties;

    public RabbitMqDemoController(RabbitTemplate rabbitTemplate, ApplicationProperties applicationProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.applicationProperties = applicationProperties;
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody MyMessage message) {
        rabbitTemplate.convertAndSend(applicationProperties.commandeEvenementEchange()
                ,message.routingKey()
                ,message.payload());
    }

    public record MyMessage(String routingKey, MyPayload payload) {

    }

    public record MyPayload(String content) {

    }
}
