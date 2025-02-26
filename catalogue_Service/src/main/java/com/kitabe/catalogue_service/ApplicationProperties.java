package com.kitabe.catalogue_service;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.integration.annotation.Default;

/**
 * Classe de configuration pour les propriétés de l'application, préfixées par "catalogue".
 * Exemple : catalogue.page-size dans application.properties.
 */
@ConfigurationProperties(prefix = "catalogue")
public record ApplicationProperties (
        /**
         * Taille de la page pour la pagination (minimum 1, défaut 10).
         */
        @DefaultValue("10")
        @Min(1)
        int pageSize){

}
