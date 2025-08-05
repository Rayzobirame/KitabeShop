package com.kitabe.kitabe_webapp.commande.web.utils;

import com.kitabe.commande_service.domaine.model.Addresse;
import com.kitabe.commande_service.domaine.model.Client;
import com.kitabe.commande_service.domaine.model.CommandeItems;
import com.kitabe.commande_service.domaine.model.CreerCommandeRequest;
import org.instancio.Instancio;
import org.instancio.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Classe utilitaire pour générer des payloads de test pour les commandes.
 * <p>
 * Cette classe fournit des méthodes statiques pour créer des instances de
 * {@link CreerCommandeRequest} avec des données valides ou invalides, utilisées dans les
 * tests unitaires et d'intégration. Elle utilise Instancio pour générer des objets avec des
 * valeurs personnalisées et inclut des constantes pour des ensembles de données prédéfinis.
 * </p>
 */
public class TestDataFactory {

    /** Liste de pays valides pour les adresses de livraison. */
    static final List<String> PAYS_VALIDES = List.of("Senegal", "France", "Etat-Unis");
    /** Ensemble d'items valides pour une commande. */
    static final Set<CommandeItems> COMMANDE_VALIDES =
            Set.of(new CommandeItems("392", "Lait", new BigDecimal("29.9"), 2));
    /** Ensemble d'items invalides ou utilisé comme référence dans certains cas de test. */
    static final Set<CommandeItems> COMMANDE_INVALIDES =
            Set.of(new CommandeItems("103", "Livres", new BigDecimal("22.0"), 3));

    /**
     * Crée une requête valide pour une commande.
     * <p>
     * Génère un objet {@link CreerCommandeRequest} avec des données valides pour tous les champs,
     * utilisant Instancio pour personnaliser les valeurs. Cette méthode est utile pour tester
     * des scénarios où une commande doit être acceptée avec succès.
     * </p>
     *
     * @return Une instance valide de {@link CreerCommandeRequest}.
     */
    public static CreerCommandeRequest creerRequestCommandeValide() {
        return Instancio.of(CreerCommandeRequest.class)
                .supply(Select.field("client.email"), () -> "ibou95@gmail.com") // Accesseur direct pour record
                .supply(Select.field("client.nom"), () -> "Traore")
                .supply(Select.field("client.prenom"), () -> "Ibou")
                .supply(Select.field("client.telephone"), () -> "123456789")
                .set(Select.field("items"), COMMANDE_VALIDES)
                .supply(Select.field("livraisonAddresse.addresse1"), () -> "123 Rue Exemple")
                .supply(Select.field("livraisonAddresse.ville"), () -> "Haire")
                .supply(Select.field("livraisonAddresse.region"), () -> "Matam")
                .supply(Select.field("livraisonAddresse.pays"), gen -> gen.oneOf(PAYS_VALIDES))
                .supply(Select.field("livraisonAddresse.addressePostal"), () -> "12345")
                .create();
    }

    /**
     * Crée une requête avec un client invalide (ex. téléphone manquant).
     */
    /**
     * Crée une requête avec un client invalide (ex. téléphone ou prénom manquant).
     * <p>
     * Génère un objet {@link CreerCommandeRequest} où le champ {@code client} contient des données
     * invalides, comme un téléphone ou un prénom null. Cette méthode est utilisée pour tester
     * les cas où la validation du client doit échouer.
     * </p>
     *
     * @return Une instance de {@link CreerCommandeRequest} avec un client invalide.
     */
    public static CreerCommandeRequest creerRequestCommandeAvecClientInvalide() {
        Client clientInvalide = new Client("Traore", null, "johntra@gmail.com", null); // Téléphone manquant
        Addresse adresse = new Addresse("123 Rue Exemple", null, "Dakar", "Dakar", "Senegal", "12345");
        return new CreerCommandeRequest(COMMANDE_VALIDES, clientInvalide, adresse);
    }

    // Méthode temporaire pour adresse invalide
    /**
     * Crée une requête avec une adresse de livraison invalide (ex. adresse1 manquante).
     * <p>
     * Génère un objet {@link CreerCommandeRequest} où l'adresse de livraison contient des champs
     * invalides, comme une adresse1 null. Cette méthode est utilisée pour tester les cas où la
     * validation de l'adresse doit échouer.
     * </p>
     *
     * @return Une instance de {@link CreerCommandeRequest} avec une adresse invalide.
     */
    public static CreerCommandeRequest creerRequestCommandeAvecAddresseLivraisonInvalide() {
        Client clientInvalide = new Client("Traore", "John", "johntra@gmail.com", "776382811"); // Téléphone manquant
        Addresse adresse = new Addresse(null, null, "Dakar", "Dakar", "Senegal", "12345");
        return new CreerCommandeRequest(COMMANDE_VALIDES, clientInvalide, adresse);
    }

    /**
     * Crée une requête sans items (liste d'items null).
     * <p>
     * Génère un objet {@link CreerCommandeRequest} où le champ {@code items} est null, simulant
     * une commande sans produits. Cette méthode est utilisée pour tester les cas où une commande
     * doit être rejetée en raison de l'absence d'items.
     * </p>
     *
     * @return Une instance de {@link CreerCommandeRequest} sans items.
     */
    public static CreerCommandeRequest creerRequestCommandeSansItems() {
        Client clientInvalide = new Client("Traore", "John", "johntra@gmail.com", "776382811"); // Téléphone manquant
        Addresse adresse = new Addresse("Thiaroye", "Haire", "Dakar", "Dakar", "Senegal", "12345");
        return new CreerCommandeRequest(null, clientInvalide, adresse);
    }

    /* public static String creerRequestCommandeAvecClientInvalide() {
        return """
                {
                    "client": {
                        "prenom": "issa",
                        "nom": "camara",
                        "phone": "7783992234" // Email manquant pour provoquer une erreur
                    },
                    "livraisonAddresse": {
                        "addresse1": "Zack mbao",
                        "addresse2": "Thiaroye",
                        "ville": "Waounde",
                        "region": "Matam",
                        "pays": "Senegal"
                    },
                    "items": [
                        {
                            "code": "P393",
                            "nom": "Chaussures",
                            "prix": 30.0,
                            "quantite": 2
                        }
                    ]
                }
                """;
    }*/

    /**
     * Crée un payload JSON pour une commande valide (pour d'autres tests).
     * @return Payload JSON sous forme de chaîne.
     */
    /* public static String creerRequestCommandeValide() {
        return """
                {
                    "client": {
                        "prenom": "issa",
                        "nom": "camara",
                        "email": "issa_cmr@gmail.com",
                        "phone": "7783992234"
                    },
                    "livraisonAddresse": {
                        "addresse1": "Zack mbao",
                        "addresse2": "Thiaroye",
                        "ville": "Waounde",
                        "region": "Matam",
                        "pays": "Senegal"
                    },
                    "items": [
                        {
                            "code": "P393",
                            "nom": "Chaussures",
                            "prix": 30.0,
                            "quantite": 2
                        }
                    ]
                }
                """;
    }*/
}
