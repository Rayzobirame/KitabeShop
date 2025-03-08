package com.kitabe.commande_service.domaine.model;

import com.kitabe.commande_service.domaine.CommandeEntite;
import com.kitabe.commande_service.domaine.CommandeItemsEntite;
import com.kitabe.commande_service.domaine.CommandeStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CommandeMapper {
    public static CommandeEntite convertToEntite(CreerCommandeRequest creerCommandeRequest){
        CommandeEntite commandeNouvelle = new CommandeEntite();
        commandeNouvelle.setCommandeNum(UUID.randomUUID().toString());
        commandeNouvelle.setStatus(CommandeStatus.NOUVEAU);
        commandeNouvelle.setClient(creerCommandeRequest.client());
        commandeNouvelle.setAddresseLivraison(creerCommandeRequest.livraisonAddresse());
        Set<CommandeItemsEntite> commandeItems = new HashSet<>();
        for(CommandeItems items : creerCommandeRequest.items()){
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
}
