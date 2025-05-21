package com.kitabe.commande_service.domaine;

import com.kitabe.commande_service.domaine.model.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Mappe les entités de commande en différents types d'événements de commande.
 * Cette classe utilitaire est utilisée pour transformer une entité {@link CommandeEntite} en divers événements
 * tels que {@link CreerCommandeEvenement}, {@link CommandeDelivrerEvenement}, {@link CommandeErreurEvenement},
 * ou {@link CommandeAnnuleeEvenement}, qui peuvent ensuite être publiés ou traités.
 */
public class CommandeEvenementMapper {

    /**
     * Construit un événement de création de commande à partir d'une entité de commande.
     * Cette méthode génère un événement avec un identifiant unique et remplit ses champs à partir des données
     * de l'entité {@link CommandeEntite}.
     *
     * @param commande L'entité de commande ({@link CommandeEntite}) à transformer en événement.
     * @return Un événement de création de commande ({@link CreerCommandeEvenement}) prêt à être publié.
     */
    static CreerCommandeEvenement buildCommandeCreerSurEvenement(CommandeEntite commande) {
        return new CreerCommandeEvenement(
                UUID.randomUUID().toString(),
                commande.getCommandeNum(),
                getCommandeItems(commande),
                commande.getClient(),
                commande.getLivraisonAddresse(),
                LocalDateTime.now());
    }

    /**
     * Construit un événement de livraison de commande à partir d'une entité de commande.
     * Cette méthode génère un événement avec un identifiant unique pour indiquer qu'une commande a été livrée,
     * en utilisant les données de l'entité {@link CommandeEntite}.
     *
     * @param commande L'entité de commande ({@link CommandeEntite}) à transformer en événement.
     * @return Un événement de livraison de commande ({@link CommandeDelivrerEvenement}) prêt à être publié.
     */
    static CommandeDelivrerEvenement buildCommandeDelivrerEvenement(CommandeEntite commande) {
        return new CommandeDelivrerEvenement(
                UUID.randomUUID().toString(),
                commande.getCommandeNum(),
                getCommandeItems(commande),
                commande.getClient(),
                commande.getLivraisonAddresse(),
                LocalDateTime.now());
    }

    /**
     * Construit un événement d'erreur de commande à partir d'une entité de commande et d'une raison d'erreur.
     * Cette méthode génère un événement avec un identifiant unique pour signaler une erreur lors du traitement
     * de la commande, en incluant la raison de l'échec.
     *
     * @param commande L'entité de commande ({@link CommandeEntite}) à transformer en événement.
     * @param raison   La raison de l'échec du traitement de la commande.
     * @return Un événement d'erreur de commande ({@link CommandeErreurEvenement}) prêt à être publié.
     */
    static CommandeErreurEvenement buildCommandeErreurEvenement(CommandeEntite commande, String raison) {
        return new CommandeErreurEvenement(
                UUID.randomUUID().toString(),
                commande.getCommandeNum(),
                getCommandeItems(commande),
                commande.getClient(),
                commande.getLivraisonAddresse(),
                raison,
                LocalDateTime.now());
    }
    /**
     * Construit un événement d'annulation de commande à partir d'une entité de commande et d'une raison d'annulation.
     * Cette méthode génère un événement avec un identifiant unique pour indiquer qu'une commande a été annulée,
     * en incluant la raison de l'annulation.
     *
     * @param commande L'entité de commande ({@link CommandeEntite}) à transformer en événement.
     * @param raison   La raison de l'annulation de la commande.
     * @return Un événement d'annulation de commande ({@link CommandeAnnuleeEvenement}) prêt à être publié.
     */
    static CommandeAnnuleeEvenement buildCommandeAnnuleeEvenement(CommandeEntite commande, String raison) {
        return new CommandeAnnuleeEvenement(
                UUID.randomUUID().toString(),
                commande.getCommandeNum(),
                getCommandeItems(commande),
                commande.getClient(),
                commande.getLivraisonAddresse(),
                raison,
                LocalDateTime.now());
    }

    /**
     * Extrait les éléments de commande d'une entité de commande et les transforme en un ensemble de {@link CommandeItems}.
     * Cette méthode mappe chaque élément de la commande (produits, quantités, etc.) en un objet {@link CommandeItems}.
     *
     * @param commandes L'entité de commande ({@link CommandeEntite}) contenant les éléments à transformer.
     * @return Un ensemble de {@link CommandeItems} représentant les éléments de la commande.
     */
    private static Set<CommandeItems> getCommandeItems(CommandeEntite commandes) {
        return commandes.getCommandeItems().stream()
                .map(item -> new CommandeItems(item.getCode(), item.getNom(), item.getPrix(), item.getQuantite()))
                .collect(Collectors.toSet());
    }
}
