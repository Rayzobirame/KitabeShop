package com.kitabe.commande_service.domaine.model;

import com.kitabe.commande_service.domaine.CommandeEntite;
import com.kitabe.commande_service.domaine.CommandeItemsEntite;
import com.kitabe.commande_service.domaine.CommandeStatus;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Mappe les objets de requête et les entités de commande dans l'application KitabeShop.
 * Cette classe utilitaire fournit des méthodes pour convertir une requête de création de commande
 * ({@link CreerCommandeRequest}) en une entité de commande ({@link CommandeEntite}), et pour transformer
 * une entité de commande en un objet de transfert de données ({@link CommandeDTO}) destiné à la couche de présentation.
 */
public class CommandeMapper {

    /**
     * Convertit une requête de création de commande en une entité de commande.
     * Cette méthode crée une nouvelle entité {@link CommandeEntite} à partir des données fournies dans
     * la requête {@link CreerCommandeRequest}. Elle initialise le numéro de commande avec un UUID unique,
     * définit le statut initial à {@link CommandeStatus#NOUVEAU}, et mappe les articles de la commande
     * en entités {@link CommandeItemsEntite}.
     *
     * @param creerCommandeRequest La requête contenant les détails de la commande à créer ({@link CreerCommandeRequest}).
     * @return Une entité de commande ({@link CommandeEntite}) prête à être persistée.
     */
    public static CommandeEntite convertToEntite(CreerCommandeRequest creerCommandeRequest) {
        CommandeEntite commandeNouvelle = new CommandeEntite();
        commandeNouvelle.setCommandeNum(UUID.randomUUID().toString());
        commandeNouvelle.setStatus(CommandeStatus.NOUVEAU);
        commandeNouvelle.setClient(creerCommandeRequest.client());
        commandeNouvelle.setLivraisonAddresse(creerCommandeRequest.livraisonAddresse());
        Set<CommandeItemsEntite> commandeItems = new HashSet<>();
        for (CommandeItems items : creerCommandeRequest.items()) {
            CommandeItemsEntite commandeItem = new CommandeItemsEntite();
            commandeItem.setCode(items.code());
            commandeItem.setNom(items.nom());
            commandeItem.setPrix(items.prix());
            commandeItem.setQuantite(items.quantite());
            commandeItem.setCommande(commandeNouvelle);
            commandeItems.add(commandeItem);
        }
        commandeNouvelle.setCommandeItems(commandeItems);

        return commandeNouvelle;
    }

    /**
     * Convertit une entité de commande en un objet de transfert de données (DTO).
     * Cette méthode transforme une entité {@link CommandeEntite} en un {@link CommandeDTO} pour une utilisation
     * dans la couche de présentation. Elle mappe les articles de la commande ({@link CommandeItemsEntite})
     * en objets {@link CommandeItems} et inclut toutes les informations pertinentes de la commande.
     *
     * @param commande L'entité de commande ({@link CommandeEntite}) à transformer.
     * @return Un objet de transfert de données ({@link CommandeDTO}) représentant la commande.
     */
    public static CommandeDTO convertToDTO(CommandeEntite commande) {
        Set<CommandeItems> commandeItems = commande.getCommandeItems().stream()
                .map(item -> new CommandeItems(item.getCode(), item.getNom(), item.getPrix(), item.getQuantite()))
                .collect(Collectors.toSet());
        return new CommandeDTO(
                commande.getCommandeNum(),
                commande.getPseudo(),
                commandeItems,
                commande.getClient(),
                commande.getLivraisonAddresse(),
                commande.getStatus(),
                commande.getCommentaire(),
                commande.getCreationDate());
    }
}
