package com.kitabe.commande_service.domaine;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Service de sécurité pour extraire les informations de l'utilisateur authentifié.
 *
 * Ce service fournit des méthodes utilitaires pour accéder aux informations
 * contenues dans le JWT de l'utilisateur actuellement authentifié.
 *
 */
@Service
public class SecurityService {


    /**
     * Récupère le nom d'utilisateur de l'utilisateur actuellement authentifié.
     *
     * Cette méthode extrait le claim "preferred_username" du JWT présent
     * dans le contexte de sécurité Spring Security.
     *
     * @return le nom d'utilisateur (pseudo) de l'utilisateur connecté
     * @throws ClassCastException si l'authentification n'est pas de type JwtAuthenticationToken
     * @throws NullPointerException si aucune authentification n'est présente dans le contexte
     *
     * @apiNote Cette méthode suppose que l'authentification est basée sur JWT
     *          et que le claim "preferred_username" existe dans le token
     */
    public String getLoginUsername() {

        //return "user";
        // Récupération du token JWT depuis le contexte de sécurité
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getPrincipal();

        /*
         * Exemples d'autres claims disponibles dans le JWT :
         *
         * String pseudo = jwt.getClaimAsString("preferred_username");
         * String email = jwt.getClaimAsString("email");
         * String nom = jwt.getClaimAsString("nom");
         * String token = jwt.getClaimAsString("token");
         * String authorities = jwt.getClaimAsString("authorities");
         */

        return jwt.getClaimAsString("preferred_username");
    }
}
