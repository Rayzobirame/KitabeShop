package com.kitabe.commande_service.web.controlleurs;

import com.kitabe.commande_service.AbstractIT;
import com.kitabe.commande_service.domaine.model.CommandeSommaire;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static com.kitabe.commande_service.web.utils.TestDataFactory.creerRequestCommandeAvecClientInvalide;
import static io.restassured.RestAssured.given; // Ajout de l'import manquant
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue; // Import corrigé (sans shaded)
import static org.junit.jupiter.api.Assertions.*; // Import inutilisé, mais conservé pour d'autres tests futurs

/**
 * Tests pour le contrôleur CommandeController.
 * Hérite de AbstractIT pour la configuration des tests d'intégration.
 */
/**
 * Tests d'intégration pour le contrôleur {@link CommandeController}.
 * Cette classe utilise RestAssured pour tester les points d'entrée REST de l'API des commandes.
 * Les tests vérifient le comportement des endpoints GET pour récupérer les commandes d'un utilisateur,
 * soit sous forme de sommaire, soit sous forme de détails complets.
 */
@Sql("/commande_test.sql")
class CommandeControllerTest extends AbstractIT {

    @Nested
    class CreerCommandeTests {

        /**
         * Teste la création réussie d'une commande via l'API.
         * Vérifie que la réponse est 201 (CREATED) et que le numéro de commande est non nul.
         */
        @Test
        void creerCommandeAvecSucces() {
            mockGetProduitByCode("LIV-SCI-003","Neuromancien",new BigDecimal("13.25"));
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
                                "code": "LIV-SCI-003",
                                "nom": "Neuromancien",
                                "prix": 13.25,
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
            var payload = creerRequestCommandeAvecClientInvalide();
            given()
                    .contentType(ContentType.JSON) // Définit le type de contenu comme JSON
                    .body(payload)                // Envoie le payload JSON
                    .when()
                    .post("/api/commande")       // Appelle l'endpoint POST
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    /**
     * Tests pour l'endpoint GET /api/commande.
     * Cette classe imbriquée regroupe les tests pour la récupération d'un sommaire des commandes
     * d'un utilisateur authentifié.
     */
    @Nested
    class getCommandeTest{

        /**
         * Vérifie que l'endpoint GET /api/commande retourne avec succès un sommaire des commandes.
         * Ce test simule une requête GET pour récupérer les commandes d'un utilisateur authentifié
         * et vérifie que la réponse a un code de statut 200 et contient exactement 2 commandes.
         * Les données de test sont insérées via Flyway (V2__insert_test_data.sql).
         */
        @Test
        void shouldretourneCommandeReussi(){
            List<CommandeSommaire> sommaireCommande = given()
                    .when()
                    .get("/api/commande")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body().as(new TypeRef<>(){});
            assertThat(sommaireCommande).hasSize(4);
        }
    }

    /**
     * Tests pour l'endpoint GET /api/commande/{commandeNum}.
     * Cette classe imbriquée regroupe les tests pour la récupération des détails d'une commande spécifique
     * identifiée par son numéro de commande.
     */
    @Nested
    class getCommandebyCommandeNum{
        // Numéro de commande utilisé pour les tests (correspond à une commande insérée via Flyway)
        String commandeNum = "364ca8b2-5255-4a2b-b042-341dae1fa617";

        /**
         * Vérifie que l'endpoint GET /api/commande/{commandeNum} retourne avec succès les détails d'une commande.
         * Ce test simule une requête GET pour récupérer les détails d'une commande spécifique
         * (identifiée par son numéro de commande) pour un utilisateur authentifié.
         * Il vérifie que :
         * - La réponse a un code de statut 200.
         * - Le champ "commandeNum" dans la réponse correspond au numéro de commande attendu.
         * - La commande contient exactement 2 articles (basé sur les données insérées via Flyway).
         */
        @Test
        void shouldretourneCommandeReussi(){
            given()
                    .when()
                    .get("/api/commande/{commandeNum}", commandeNum)
                    .then()
                    .statusCode(200)
                    .body("commandeNum", equalTo(commandeNum))
                    .body("items.size()",equalTo(2));
        }
    }
}