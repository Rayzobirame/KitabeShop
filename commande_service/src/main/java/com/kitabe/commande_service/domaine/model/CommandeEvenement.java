package com.kitabe.commande_service.domaine.model;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Interface commune pour tous les événements de commande.
 * Cette interface définit les champs communs que tous les événements de commande doivent fournir.
 */
public interface CommandeEvenement {
    /**
     * @return L'identifiant unique de l'événement.
     */
    String evenementId();

    /**
     * @return Le numéro de la commande associée à l'événement.
     */
    String commandeNum();

    /**
     * @return L'ensemble des articles de la commande.
     */
    Set<CommandeItems> items();

    /**
     * @return Les informations du client.
     */
    Client client();

    /**
     * @return L'adresse de livraison.
     */
    Addresse addresse();

    /**
     * @return La date et l'heure de création de l'événement.
     */
    LocalDateTime creerLe();

    /**
     * @return La raison de l'événement (optionnelle, peut être null pour certains types d'événements).
     */
    default String raison() {
        return null;
    }
}
