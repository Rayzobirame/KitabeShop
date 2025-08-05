package com.kitabe.kitabe_webapp.commande.web.controllers;

import com.kitabe.commande_service.domaine.CommandeService;
import com.kitabe.commande_service.domaine.SecurityService;
import com.kitabe.kitabe_webapp.Controllers.CommandeController;
import com.kitabe.kitabe_webapp.commande.CreerCommandeRequest;
import com.kitabe.kitabe_webapp.commande.CommandeItems;
import com.kitabe.kitabe_webapp.commande.Client;
import com.kitabe.kitabe_webapp.commande.Addresse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wiremock.org.eclipse.jetty.util.component.Dumpable.named;

/**
 * Tests unitaires pour le contrôleur de commandes (CommandeController).
 *
 * Cette classe de test utilise l'annotation @WebMvcTest pour créer un contexte
 * Spring Boot minimal focalisé sur la couche web. Elle teste uniquement la
 * logique du contrôleur en mockant toutes les dépendances.
 *
 * Fonctionnalités testées :
 * - Validation des données d'entrée (Bean Validation)
 * - Gestion des erreurs HTTP appropriées
 * - Sécurité et authentification
 * - Sérialisation/désérialisation JSON
 *
 * Types de tests :
 * - Tests paramétrés pour couvrir différents cas d'invalidité
 * - Tests avec utilisateur simulé pour la sécurité
 * - Tests de validation des payloads JSON
 *
 * @author Votre nom
 * @version 1.0
 * @since 2024
 *
 * @see WebMvcTest
 * @see ParameterizedTest
 * @see MockBean
 * @see WithMockUser
 */
@WebMvcTest(controllers = CommandeController.class)
public class CommandeControllerUnitTest {

    // ========================================
    // MOCKS ET DÉPENDANCES
    // ========================================

    /**
     * Mock du service de commandes pour isoler les tests du contrôleur.
     *
     * Ce mock permet de tester uniquement la logique du contrôleur sans
     * dépendre de l'implémentation réelle du service. Les comportements
     * sont définis dans chaque test selon les besoins.
     */
    @MockBean
    private CommandeService commandeService;

    /**
     * Mock du service de sécurité pour simuler l'authentification.
     *
     * Ce mock permet de contrôler les informations d'authentification
     * retournées dans les tests, notamment le nom d'utilisateur connecté.
     */
    @MockBean
    private SecurityService securityService;

    /**
     * Instance MockMvc pour simuler les requêtes HTTP.
     *
     * MockMvc permet d'effectuer des requêtes HTTP simulées vers le contrôleur
     * sans démarrer un serveur web complet. Il teste la couche web complète
     * incluant la sérialisation, la validation et la gestion des erreurs.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * ObjectMapper pour la sérialisation/désérialisation JSON.
     *
     * Utilisé pour convertir les objets Java en JSON dans les requêtes de test.
     * Spring Boot configure automatiquement cette instance avec les mêmes
     * paramètres que l'application.
     */
    @Autowired
    private ObjectMapper objectMapper;

    // ========================================
    // CONFIGURATION DES TESTS
    // ========================================

    /**
     * Configuration exécutée avant chaque test.
     *
     * Configure les comportements par défaut des mocks qui sont nécessaires
     * pour tous les tests de cette classe. Cette configuration évite la
     * duplication de code dans chaque test individuel.
     *
     * Configurations appliquées :
     * - SecurityService retourne un utilisateur de test fixe
     * - Prépare l'état des mocks pour les tests
     *
     * @apiNote Cette méthode est appelée avant chaque méthode de test,
     *          garantissant un état propre pour chaque test
     */
    @BeforeEach
    void setUp() {
        // Configuration du mock SecurityService pour retourner un utilisateur de test
        given(securityService.getLoginUsername()).willReturn("birame");
    }

    // ========================================
    // TESTS DE VALIDATION
    // ========================================

    /**
     * Test paramétré pour valider le rejet des requêtes invalides.
     *
     * Ce test vérifie que le contrôleur retourne correctement un code d'erreur
     * HTTP 400 (Bad Request) lorsque les données d'entrée ne respectent pas
     * les contraintes de validation définies dans les DTOs.
     *
     * Scénarios testés :
     * - Commande avec informations client manquantes/invalides
     * - Commande avec adresse de livraison manquante/invalide
     * - Commande sans articles (liste vide)
     *
     * Comportement attendu :
     * - Le contrôleur doit retourner HTTP 400
     * - La validation Bean Validation doit intercepter les erreurs
     * - Le service ne doit pas être appelé si la validation échoue
     *
     * @param request La requête de création de commande à tester (peut être invalide)
     * @throws Exception Si une erreur inattendue survient pendant le test
     *
     * @see ParameterizedTest
     * @see MethodSource
     * @see WithMockUser
     */
    @ParameterizedTest(name = "[{index}] - {0}")
    @MethodSource("creerCommandeRequestProvider")
    @WithMockUser
    void shouldReturnBadRequestWhenPayLoadIsInvalid(CreerCommandeRequest request) throws Exception {
        // GIVEN - Configuration du mock pour retourner null (peu importe car validation échoue avant)
        given(commandeService.creerCommande(eq("birame"), any(com.kitabe.commande_service.domaine.model.CreerCommandeRequest.class)))
                .willReturn(null);

        // WHEN & THEN - Exécution de la requête et vérification du résultat
        mockMvc.perform(post("/api/commande")
                        .with(csrf()) // Ajout du token CSRF requis par Spring Security
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Vérification du code d'erreur HTTP 400
    }

    // ========================================
    // FOURNISSEURS DE DONNÉES DE TEST
    // ========================================

    /**
     * Fournit les données de test pour les tests paramétrés de validation.
     *
     * Cette méthode génère un Stream d'Arguments contenant différents cas
     * de requêtes invalides pour tester la validation. Chaque cas de test
     * a un nom descriptif et une requête avec des données invalides spécifiques.
     *
     * Cas de test générés :
     * 1. Client invalide (null) - Teste la validation @Valid sur le client
     * 2. Adresse invalide (null) - Teste la validation @Valid sur l'adresse
     * 3. Articles vides - Teste la validation @NotEmpty sur la liste d'articles
     *
     * @return Un Stream d'Arguments pour alimenter les tests paramétrés
     *
     * @apiNote Cette méthode doit être statique pour être utilisée comme @MethodSource
     * @apiNote Les noms des cas de test apparaissent dans les rapports de test
     *
     * @see MethodSource
     * @see Arguments
     */
    static Stream<Arguments> creerCommandeRequestProvider() {
        return Stream.of(
                // Cas 1: Client invalide (null)
                // Teste la validation @Valid sur le champ client
                arguments(named("Commande avec client invalide",
                        new CreerCommandeRequest(
                                Set.of(new CommandeItems("P001", "Livre Narnia",new BigDecimal("13.25"),2)), // Articles valides
                                null, // Client invalide (null)
                                new Addresse("123 rue", "Paris 12", "Paris","ile de France","France","75001")))), // Adresse valide

                // Cas 2: Adresse invalide (null)
                // Teste la validation @Valid sur le champ livraisonAddresse
                arguments(named("Commande avec adresse invalide",
                        new CreerCommandeRequest(
                                Set.of(new CommandeItems("P001", "Une Si Longue Lettre",new BigDecimal("13.25"),2)), // Articles valides
                                new Client("John", "Doe", "john@email.com","773929332"), // Client valide
                                null))), // Adresse invalide (null)

                // Cas 3: Articles vides
                // Teste la validation @NotEmpty sur le champ items
                arguments(named("Commande sans articles",
                        new CreerCommandeRequest(
                                Set.of(), // Set vide - viole @NotEmpty
                                new Client("John", "Doe", "john@email.com","778382273"), // Client valide
                                new Addresse("123 rue", "Paris", "Villeneuf Lagarenne","Iles de France","France","75001")))) // Adresse valide
        );
    }
}