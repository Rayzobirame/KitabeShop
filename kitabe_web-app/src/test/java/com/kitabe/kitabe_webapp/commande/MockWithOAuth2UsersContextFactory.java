package com.kitabe.kitabe_webapp.commande;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Factory pour créer un contexte de sécurité fictif lors des tests d'intégration avec OAuth2/JWT.
 *
 * Cette classe implémente WithSecurityContextFactory pour générer un contexte de sécurité
 * Spring Security avec un JWT fictif, permettant de tester les endpoints sécurisés sans
 * avoir besoin d'un serveur Keycloak réel.
 */
 public class MockWithOAuth2UsersContextFactory implements WithSecurityContextFactory<MockWithOAuth2Users> {

    /**
     * Crée un contexte de sécurité Spring Security basé sur les informations fournies
     * dans l'annotation MockWithOAuth2Users.
     *
     * @param withUser l'annotation contenant les informations de l'utilisateur fictif
     *                 (nom d'utilisateur, rôles, ID, etc.)
     * @return SecurityContext configuré avec un JWT fictif et les autorités correspondantes
     * @throws IllegalArgumentException si le nom d'utilisateur est null ou si un rôle
     *                                 commence déjà par "ROLE_"
     */
    @Override
    public SecurityContext createSecurityContext(MockWithOAuth2Users withUser) {
        // Récupération du nom d'utilisateur avec fallback sur value() si username() est vide
        String username = StringUtils.hasLength(withUser.username()) ? withUser.username(): withUser.value();
        // Validation : le nom d'utilisateur ne peut pas être null
        if(username == null){
            throw new IllegalArgumentException(
                    withUser +"username ne peut pas etre null de meme que pour le nom");
        }
        // Construction de la liste des autorités à partir des rôles
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(String role : withUser.role()){
            // Vérification que le rôle ne commence pas déjà par "ROLE_"
            if(role.startsWith("ROLE_")){
                throw new IllegalArgumentException(
                        withUser +"Role ne peut pas commencer par Role_Got"+role);
            }
            // Ajout automatique du préfixe "ROLE_" requis par Spring Security
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
        }
        // Création des claims JWT avec les informations utilisateur
        Map<String , Object> claims = Map.of("preferred_username", username, "userId", withUser.id(),"realm_access",authorities);
        Map<String , Object> header = Map.of("header", "mock");
        // Création du JWT fictif avec une durée de validité de 5 minutes
        Jwt jwt = new Jwt("mock_jwt_token", Instant.now(),Instant.now().plusSeconds(300),claims,header);
        // Création de l'objet Authentication avec le JWT
        Authentication auth = new JwtAuthenticationToken(jwt, authorities);
        // Création et configuration du contexte de sécurité
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        return securityContext;
    }
}
