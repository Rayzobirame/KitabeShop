package com.kitabe.catalogue_service.domaine;

import java.util.List;

/**
 * Record générique pour encapsuler les résultats paginés d'une requête.
 * @param <T> Type des éléments (ex. Produit).
 */
public record PagedResult<T>(
        List<T> data, // Liste des éléments de la page actuelle
        Long totalElements, // Nombre total d'éléments dans la base
        int totalPages, // Nombre total de pages
        int pageNumber, // Numéro de la page actuelle (commence à 1)
        boolean isFirst, // Vrai si première page
        boolean isLast, // Vrai si dernière page
        boolean hasNext, // Vrai s'il y a une page suivante
        boolean hasPrevious) {} // Vrai s'il y a une page précédente
