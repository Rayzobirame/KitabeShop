package com.kitabe.kitabe_webapp.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Service utilitaire pour gérer les opérations de sécurité OAuth2
 * Permet de récupérer facilement les informations d'authentification
 */
// Marque cette classe comme un service Spring géré par le conteneu
@Service
public class SecurityHelper {
    /**
     * Service Spring Security qui gère les clients OAuth2 autorisés
     * Permet de stocker et récupérer les tokens d'accès des utilisateurs
     */
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClient;

    public SecurityHelper(OAuth2AuthorizedClientService oAuth2AuthorizedClient) {
        this.oAuth2AuthorizedClient = oAuth2AuthorizedClient;
    }

    /**
     * Récupère le token d'accès OAuth2 de l'utilisateur actuellement authentifié
     *
     * @return String Le token d'accès JWT ou null si l'utilisateur n'est pas authentifié via OAuth2
     *
     * Utilisation typique :
     * - Appels API vers des microservices backend
     * - Transmission du contexte de sécurité entre services
     * - Vérification des permissions utilisateur
     */
    public String getAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken)) {
            System.out.println("Aucune authentification OAuth2 détectée");
            return null;
        }
        OAuth2AuthorizedClient client = oAuth2AuthorizedClient.loadAuthorizedClient(
                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(),
                oAuth2AuthenticationToken.getName()
        );
        if (client == null) {
            System.out.println("Client OAuth2 null pour registrationId: " + oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
        }
        return client != null ? client.getAccessToken().getTokenValue() : null;
    }

    /**
     * Récupère les informations de l'utilisateur connecté
     */
    public String getConnectedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * Vérifie si l'utilisateur est connecté via OAuth2
     */
    public boolean isOAuth2Authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication instanceof OAuth2AuthenticationToken && authentication.isAuthenticated();
    }
}
