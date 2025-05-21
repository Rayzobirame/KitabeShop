package com.kitabe.commande_service.web.controlleurs;

import static com.kitabe.commande_service.web.utils.TestDataFactory.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitabe.commande_service.domaine.CommandeService;
import com.kitabe.commande_service.domaine.SecurityService;
import com.kitabe.commande_service.domaine.model.CreerCommandeRequest;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommandeController.class)
@ExtendWith(MockitoExtension.class)
public class CommandeControllerUnitTest {

    /** Service de commande mocké utilisé par le contrôleur. */
    @MockBean
    private CommandeService commandeService;

    /** Service de sécurité mocké pour simuler l'authentification. */
    @MockBean
    private SecurityService securityService;

    /** Instance de MockMvc pour simuler les requêtes HTTP. */
    @Autowired
    private MockMvc mockMvc;

    /** Mapper JSON pour sérialiser/désérialiser les objets en JSON. */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Configure l'environnement de test avant chaque exécution.
     * <p>
     * Cette méthode simule le comportement de {@link SecurityService#getLoginUsername()}
     * pour renvoyer un utilisateur fixe ("birame"), permettant de tester le contrôleur
     * dans un contexte d'utilisateur authentifié.
     * </p>
     */
    @BeforeEach
    void setUp() {
        given(securityService.getLoginUsername()).willReturn("birame");
    }

    /**
     * Teste que les requêtes avec des payloads invalides renvoient un statut 400 (Bad Request).
     * <p>
     * Ce test utilise une approche paramétrée pour vérifier plusieurs scénarios d'invalidité,
     * tels qu'un client invalide, une adresse de livraison invalide ou une commande sans items.
     * Le service {@link CommandeService#creerCommande(String, CreerCommandeRequest)} est mocké
     * pour renvoyer {@code null}, simulant un échec potentiel. La requête POST est envoyée à
     * l'endpoint "/api/commande" avec un payload JSON, et le statut HTTP attendu est 400.
     * </p>
     *
     * @param name Nom du scénario de test (pour lisibilité dans les rapports).
     * @param request Le payload de la requête {@link CreerCommandeRequest} à tester.
     * @throws Exception Si une erreur survient lors de l'exécution de la requête HTTP.
     */

    /*Description : Annotation de JUnit 5 pour exécuter un test avec plusieurs ensembles de paramètres.
    Le paramètre name personnalise le nom des exécutions dans les rapports (par exemple, "0 - commande avec client invalide").
     Utilisation dans shouldReturnBadRequestWhenCommandePayloadIsInvalid :
     Permet de tester plusieurs scénarios d’invalidité avec une seule méthode.*/

    /*Description : Indique la méthode qui fournit les données pour un test paramétré.
    Doit retourner un Stream, Iterable, ou tableau d’arguments.
    Utilisation dans shouldReturnBadRequestWhenCommandePayloadIsInvalid :
    Lie le test à la méthode creerCommandeRequestProvider pour obtenir les données.*/
    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("creerCommandeRequestProvider")
    void shouldReturnBadRequestWhenCommandePayloadIsInvalid(String name, CreerCommandeRequest request)
            throws Exception {
        given(commandeService.creerCommande(eq("birame"), any(CreerCommandeRequest.class)))
                .willReturn(null);

        mockMvc.perform(post("/api/commande")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Fournit les données pour le test paramétré {@link #shouldReturnBadRequestWhenCommandePayloadIsInvalid}.
     * <p>
     * Cette méthode retourne un flux de scénarios de test, chacun composé d'un nom descriptif
     * et d'un objet {@link CreerCommandeRequest} invalide généré par {link TestDataFactory}.
     * Les cas incluent une commande avec un client invalide, une adresse de livraison invalide,
     * et une commande sans items.
     * </p>
     *
     * @return Un {@link Stream} d'{@link Arguments} contenant les données de test.
     */
    static Stream<Arguments> creerCommandeRequestProvider() {
        return Stream.of(
                arguments("commande avec client invalide", creerRequestCommandeAvecClientInvalide()),
                arguments("commande avec adresse invalide", creerRequestCommandeAvecAddresseLivraisonInvalide()),
                arguments("Cette commande n'a pas de produit", creerRequestCommandeSansItems()));
    }
}
