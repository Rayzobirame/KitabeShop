package com.kitabe.commande_service.domaine.model;

import java.time.LocalDateTime;
import java.util.Set;
/**
 * Représente un événement de création de commande dans le système.
 * Ce record est utilisé pour encapsuler les informations nécessaires à la création d'une commande,
 * telles que l'identifiant de l'événement, le numéro de commande, les articles commandés, les informations
 * du client, l'adresse de livraison, et la date de création. Il est typiquement utilisé pour publier des
 * événements via RabbitMQ ou pour persister des données dans la base.
 *
 * @param evenementId L'identifiant unique de l'événement de création de commande.
 * @param commandeNum Le numéro unique de la commande associée à cet événement.
 * @param items       L'ensemble des articles ({@link CommandeItems}) inclus dans la commande.
 * @param client      Les informations du client ({@link Client}) ayant passé la commande.
 * @param addresse    L'adresse de livraison ({@link Addresse}) pour la commande.
 * @param creerLe     La date et l'heure de création de l'événement.
 */

public record CreerCommandeEvenement(
        String evenementId,
        String commandeNum,
        Set<CommandeItems> items,
        Client client,
        Addresse addresse,
        LocalDateTime creerLe
) implements CommandeEvenement{
}
