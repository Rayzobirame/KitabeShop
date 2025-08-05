package com.kitabe.kitabe_webapp.commande;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

/**
 * Annotation personnalisée utilisée pour simuler un contexte de sécurité OAuth2 dans les tests Spring Security.
 * Cette annotation permet de mocker un utilisateur authentifié avec des attributs spécifiques (ID, nom d'utilisateur,
 * et rôles) en utilisant une usine de contexte personnalisée {@link MockWithOAuth2UsersContextFactory}.
 * Elle est conçue pour être utilisée dans des tests unitaires ou d'intégration afin de simuler une authentification
 * basée sur OAuth2 sans nécessiter un serveur d'authentification réel.
 *
 * @see WithSecurityContext
 * @see MockWithOAuth2UsersContextFactory
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = MockWithOAuth2UsersContextFactory.class)
public @interface MockWithOAuth2Users {

    /**
     * L'identifiant de l'utilisateur simulé dans le contexte de sécurité.
     * Par défaut, la valeur est -1, indiquant qu'aucun ID spécifique n'est défini.
     *
     * @return l'ID de l'utilisateur
     */
    long id() default -1;
    /**
     * Le nom de l'utilisateur simulé dans le contexte de sécurité.
     * Par défaut, la valeur est "user" si aucune autre valeur n'est spécifiée.
     *
     * @return le nom de l'utilisateur
     */
    String value() default "user";
    /**
     * Le nom d'utilisateur spécifique à utiliser pour l'utilisateur simulé.
     * Si vide, la valeur par défaut est une chaîne vide ("").
     *
     * @return le nom d'utilisateur
     */
    String username() default "";
    /**
     * Le(s) rôle(s) attribué(s) à l'utilisateur simulé dans le contexte de sécurité.
     * Par défaut, le rôle "USER" est attribué si aucun autre rôle n'est spécifié.
     *
     * @return un tableau de rôles
     */
    String[] role() default {"USER"};
}
