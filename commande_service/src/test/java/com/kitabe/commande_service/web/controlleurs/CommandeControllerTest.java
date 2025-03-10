package com.kitabe.commande_service.web.controlleurs;

import com.kitabe.commande_service.AbstractIT;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given; // Ajout de l'import manquant
import static org.hamcrest.Matchers.notNullValue; // Import corrigé (sans shaded)
import static org.junit.jupiter.api.Assertions.*; // Import inutilisé, mais conservé pour d'autres tests futurs

/**
 * Tests pour le contrôleur CommandeController.
 * Hérite de AbstractIT pour la configuration des tests d'intégration.
 */
class CommandeControllerTest extends AbstractIT {

    @Nested
    class CreerCommandeTests {

        /**
         * Teste la création réussie d'une commande via l'API.
         * Vérifie que la réponse est 201 (CREATED) et que le numéro de commande est non nul.
         */
        @Test
        void creerCommandeAvecSucces() {
            // Payload JSON pour la création d'une commande
            var payload = """
                    {
                        "client": {
                            "prenom": "issa",
                            "nom": "camara",
                            "email": "issa_cmr@gmail.com",
                            "telephone": "7783992234"
                        },
                        "livraisonAddresse": {
                            "addresse1": "Zack mbao",
                            "addresse2": "Thiaroye",
                            "addressePostal":"98",
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

            // Exécute la requête POST et vérifie la réponse
            given()
                    .contentType(ContentType.JSON) // Définit le type de contenu comme JSON
                    .body(payload)                // Envoie le payload JSON
                    .when()
                    .post("/api/commande")       // Appelle l'endpoint POST
                    .then()
                    .statusCode(HttpStatus.CREATED.value()) // Vérifie que le code HTTP est 201
                    .body("commandeNum", notNullValue());   // Vérifie que commandeNum est non nul
        }

        @Test
        void shouldretourbadRequestWhenMandatoryDataIsMissing(){
            var payload = com.kitabe.commande_service.web.controlleurs.utils.TestDataFactory.creerRequestCommandeAvecClientInvalide();
            given()
                    .contentType(ContentType.JSON) // Définit le type de contenu comme JSON
                    .body(payload)                // Envoie le payload JSON
                    .when()
                    .post("/api/commande")       // Appelle l'endpoint POST
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }
}