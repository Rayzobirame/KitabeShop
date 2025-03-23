package com.kitabe.commande_service.domaine;

import com.kitabe.commande_service.domaine.model.CommandeItems;
import com.kitabe.commande_service.domaine.model.CreerCommandeEvenement;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Mappe les entités de commande en événements de création de commande.
 * Cette classe utilitaire est utilisée pour transformer une entité {@link CommandeEntite} en un événement
 * {@link CreerCommandeEvenement} qui peut être publié ou traité.
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
    static CreerCommandeEvenement buildCommandeCreerSurEvenement(CommandeEntite commande){
        return new CreerCommandeEvenement(
                UUID.randomUUID().toString(),
                commande.getCommandeNum(),
                getCommandeItems(commande),
                commande.getClient(),
                commande.getLivraisonAddresse(),
                LocalDateTime.now());
    }

    /**
     * Extrait les éléments de commande d'une entité de commande et les transforme en un ensemble de {@link CommandeItems}.
     * Cette méthode mappe chaque élément de la commande (produits, quantités, etc.) en un objet {@link CommandeItems}.
     *
     * @param commandes L'entité de commande ({@link CommandeEntite}) contenant les éléments à transformer.
     * @return Un ensemble de {@link CommandeItems} représentant les éléments de la commande.
     */
    private static Set<CommandeItems> getCommandeItems(CommandeEntite commandes){
        return commandes.getCommandeItems().stream().map(item ->new CommandeItems(item.getCode()
                , item.getNom(), item.getPrix(), item.getQuantite())).collect(Collectors.toSet());

    }
}
