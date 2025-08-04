package com.kitabe.kitabe_webapp.commande;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Représente un objet de transfert de données (DTO) pour une commande dans l'application KitabeShop.
 * Ce record est utilisé pour transmettre les détails d'une commande à la couche de présentation,
 * incluant des informations comme le numéro de commande, les articles, le client, l'adresse de livraison,
 * le statut, et un montant total calculé.
 *
 * @param commandeNum   Le numéro unique de la commande.
 * @param user          Le pseudo de l'utilisateur ayant passé la commande.
 * @param items         L'ensemble des articles de la commande ({@link CommandeItems}).
 * @param client        Les informations du client ({@link Client}).
 * @param livraisonAddresse      L'adresse de livraison ({@link Addresse}).
 * @param status        Le statut de la commande ({@link CommandeStatus}).
 * @param commentaire   Un commentaire optionnel associé à la commande.
 * @param creationDate  La date et l'heure de création de la commande.
 */
public record CommandeDTO(
        String commandeNum,
        String user,
        Set<CommandeItems> items,
        Client client,
        Addresse livraisonAddresse,
        CommandeStatus status,
        String commentaire,
        LocalDateTime creationDate) {

    /**
     * Calcule le montant total de la commande.
     * Cette méthode additionne le coût de chaque article (prix unitaire multiplié par la quantité)
     * pour obtenir le montant total de la commande. Le résultat est accessible uniquement en lecture
     * via la sérialisation JSON.
     *
     * @return Le montant total de la commande sous forme de {@link BigDecimal}.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public BigDecimal getTotalMontant() {
        return items.stream()
                .map(item -> item.prix().multiply(BigDecimal.valueOf(item.quantite())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
