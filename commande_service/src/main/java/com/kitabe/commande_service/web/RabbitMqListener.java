package com.kitabe.commande_service.web;

import com.kitabe.commande_service.web.controlleurs.RabbitMqDemoController;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqListener {

    @RabbitListener(queues = "${commande.nouvelle-commande-queue}")
    public void newCommandehandler(RabbitMqDemoController.MyPayload payload){
        System.out.println("Nouvelle commande :" +payload.content());
    }

    @RabbitListener(queues = "${commande.delivrance-commande-queue}")
    public void handleCommandeDelivrance(RabbitMqDemoController.MyPayload payload){
        System.out.println("Commande livrée :" +payload.content());
    }


}
