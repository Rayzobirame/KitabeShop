package com.kitabe.commande_service.web.controlleurs.utils;

/**
 * Classe utilitaire pour générer des payloads de test pour les commandes.
 */
public class TestDataFactory {

    /**
     * Crée un payload JSON pour une commande avec un client invalide (ex. email manquant).
     * @return Payload JSON sous forme de chaîne.
     */
    public static String creerRequestCommandeAvecClientInvalide() {
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
    }

    /**
     * Crée un payload JSON pour une commande valide (pour d'autres tests).
     * @return Payload JSON sous forme de chaîne.
     */
    public static String creerRequestCommandeValide() {
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
    }
}
