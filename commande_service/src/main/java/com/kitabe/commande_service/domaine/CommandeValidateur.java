package com.kitabe.commande_service.domaine;

import com.kitabe.commande_service.clients.catalogue.Produit;
import com.kitabe.commande_service.clients.catalogue.ProduitServiceClient;
import com.kitabe.commande_service.domaine.model.CommandeItems;
import com.kitabe.commande_service.domaine.model.CreerCommandeRequest;
import com.kitabe.commande_service.domaine.model.InvalideCommandeException;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Composant de validation des commandes.
 * <p>
 * Cette classe valide les données d'une requête de création de commande ({@link CreerCommandeRequest})
 * en vérifiant les items par rapport au catalogue de produits via {@link ProduitServiceClient}.
 * </p>
 */
@Component
public class CommandeValidateur {

    /** Logger SLF4J pour enregistrer les erreurs et informations de validation. */
    private static final Logger log = LoggerFactory.getLogger(CommandeValidateur.class);

    /** Client pour interagir avec le service de catalogue des produits. */
    private final ProduitServiceClient produitServiceClient;

    /**
     * Construit une instance de {@link CommandeValidateur} avec un client de service produit.
     *
     * @param produitServiceClient Le client pour accéder au catalogue des produits.
     */
    public CommandeValidateur(ProduitServiceClient produitServiceClient) {
        this.produitServiceClient = produitServiceClient;
    }

    /**
     * Valide une requête de création de commande.
     * <p>
     * Vérifie chaque item de la commande ({@link CommandeItems}) en s'assurant que :
     * <ul>
     *   <li>Le code du produit existe dans le catalogue.</li>
     *   <li>Le prix de l'item correspond au prix actuel du produit.</li>
     * </ul>
     * Si une validation échoue, une {@link InvalideCommandeException} est levée.
     * </p>
     *
     * @param request La requête de création de commande à valider.
     * @throws InvalideCommandeException Si un item est invalide ou si les prix ne correspondent pas.
     */
    void validateur(CreerCommandeRequest request) {
        Set<CommandeItems> commandeItems = request.items();
        for (CommandeItems commandeItem : commandeItems) {
            Produit produit = produitServiceClient
                    .getProduitByCode(commandeItem.code())
                    .orElseThrow(() -> new InvalideCommandeException("Le code du produit est invalide"));
            if (commandeItem.prix().compareTo(produit.prix()) != 0) {
                log.error(
                        "Les prix ne correspondent pas - prix actuel: {}, prix récupéré: {}",
                        produit.prix(),
                        commandeItem.prix());
                throw new InvalideCommandeException("Les prix ne correspondent pas");
            }
        }
    }
}
