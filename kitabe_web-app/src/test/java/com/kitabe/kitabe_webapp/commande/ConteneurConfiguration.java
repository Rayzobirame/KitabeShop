package com.kitabe.kitabe_webapp.commande;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Configuration des conteneurs de test pour l'application Kitabe.
 *
 * Cette classe de configuration Spring Boot Test définit et configure tous les conteneurs
 * Docker nécessaires pour les tests d'intégration de l'application. Elle utilise
 * Testcontainers pour créer un environnement de test isolé et reproductible.
 *
 * Les conteneurs configurés incluent :
 * - PostgreSQL pour la base de données
 * - RabbitMQ pour la messagerie asynchrone
 * - Keycloak pour l'authentification et l'autorisation
 *
 * Cette configuration garantit que les tests s'exécutent dans un environnement
 * identique à la production, avec des versions spécifiques des services.
 *
 * @author Votre nom
 * @version 1.0
 * @since 2024
 *
 * @see TestConfiguration
 * @see ServiceConnection
 * @see KeycloakContainer
 */
@TestConfiguration(proxyBeanMethods = false)
public class ConteneurConfiguration {

    // ========================================
    // CONSTANTES DE CONFIGURATION
    // ========================================

    /**
     * Image Docker officielle de Keycloak utilisée pour les tests.
     * Version figée pour garantir la reproductibilité des tests.
     */
    static String KEYCLOAK_IMAGE = "quay.io/keycloak/keycloak:26.2.5";

    /**
     * Chemin vers le fichier de configuration du realm Keycloak.
     * Ce fichier contient la configuration complète du realm de test
     * incluant les clients, utilisateurs, rôles et permissions.
     */
    static String realmImportFile = "/test_kitabeshop-realm.json";

    /**
     * Nom du realm Keycloak utilisé pour les tests.
     * Ce realm contient toute la configuration d'authentification spécifique aux tests.
     */
    static String realmName = "kitabeShop";

    // ========================================
    // CONFIGURATION DES CONTENEURS DE BASES DE DONNÉES
    // ========================================

    /**
     * Configure et crée un conteneur PostgreSQL pour les tests.
     *
     * Ce conteneur fournit une base de données PostgreSQL isolée pour chaque
     * exécution de tests. L'annotation @ServiceConnection permet à Spring Boot
     * de configurer automatiquement les propriétés de connexion à la base de données.
     *
     * Fonctionnalités :
     * - Base de données temporaire créée et détruite avec les tests
     * - Configuration automatique des propriétés Spring (datasource.url, etc.)
     * - Isolation complète entre les exécutions de tests
     * - Utilisation de PostgreSQL 16 Alpine pour la performance
     *
     * @return Une instance configurée de PostgreSQLContainer
     *
     * @apiNote L'annotation @ServiceConnection élimine le besoin de configurer
     *          manuellement les propriétés de connexion à la base de données
     */
    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));
    }

    // ========================================
    // CONFIGURATION DES CONTENEURS DE MESSAGERIE
    // ========================================

    /**
     * Configure et crée un conteneur RabbitMQ pour les tests.
     *
     * Ce conteneur fournit un broker de messages RabbitMQ isolé pour tester
     * les fonctionnalités de messagerie asynchrone de l'application.
     * L'interface de management est incluse pour faciliter le débogage.
     *
     * Fonctionnalités :
     * - Broker RabbitMQ temporaire avec interface web de management
     * - Configuration automatique des propriétés Spring (rabbitmq.host, etc.)
     * - Isolation complète des messages entre les tests
     * - Support des échanges, queues et routages
     *
     * @return Une instance configurée de RabbitMQContainer
     *
     * @apiNote L'image utilisée inclut le plugin de management accessible
     *          via l'interface web pour le débogage des tests
     */
    @Bean
    @ServiceConnection
    RabbitMQContainer rabbitMQContainer() {
        return new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.13-management"));
    }

    // ========================================
    // CONFIGURATION DU CONTENEUR KEYCLOAK
    // ========================================

    /**
     * Configure et crée un conteneur Keycloak pour l'authentification des tests.
     *
     * Ce conteneur fournit un serveur d'authentification Keycloak complet
     * avec un realm pré-configuré pour les tests. Le realm contient tous
     * les clients, utilisateurs et rôles nécessaires aux tests d'intégration.
     *
     * Fonctionnalités :
     * - Serveur Keycloak avec realm de test pré-configuré
     * - Import automatique de la configuration depuis un fichier JSON
     * - Utilisateurs et clients de test prêts à l'emploi
     * - Endpoints OAuth2/OpenID Connect fonctionnels
     *
     * @return Une instance configurée de KeycloakContainer
     *
     * @throws RuntimeException Si le fichier de configuration du realm est introuvable
     *
     * @apiNote Le fichier de realm doit être placé dans src/test/resources
     *          et contenir une configuration Keycloak valide
     */
    @Bean
    KeycloakContainer keycloakContainer() {
        var keycloak = new KeycloakContainer(KEYCLOAK_IMAGE)
                .withRealmImportFile(realmImportFile);
        return keycloak;
    }

    // ========================================
    // CONFIGURATION DES PROPRIÉTÉS DYNAMIQUES
    // ========================================

    /**
     * Configure les propriétés Spring dynamiques liées à Keycloak.
     *
     * Cette méthode est appelée par Spring Test pour configurer les propriétés
     * qui dépendent des conteneurs de test. Elle démarre temporairement un
     * conteneur Keycloak pour obtenir son URL et configurer les propriétés
     * OAuth2 de l'application.
     *
     * Propriétés configurées :
     * - spring.security.oauth2.resourceserver.jwt.issuer-uri : URL du serveur JWT
     *
     * @param registry Le registre des propriétés dynamiques de Spring Test
     *
     * @apiNote Cette méthode utilise un conteneur temporaire pour obtenir l'URL.
     *          En production, cette URL serait configurée statiquement.
     *
     * @see DynamicPropertySource
     * @see DynamicPropertyRegistry
     */
    @DynamicPropertySource
    static void configureKeycloakProperties(DynamicPropertyRegistry registry) {
        // Création et démarrage temporaire du conteneur pour obtenir l'URL
        var keycloak = new KeycloakContainer(KEYCLOAK_IMAGE)
                .withRealmImportFile(realmImportFile);
        keycloak.start();

        // Configuration de l'URL du serveur JWT pour Spring Security
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloak.getAuthServerUrl() + "/realms/" + realmName);
    }
}