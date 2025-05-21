package com.kitabe.commande_service.domaine.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CommandeItems(
        @NotBlank(message = "Le code est requis") String code,
        @NotBlank(message = "Le nom est requis ") String nom,
        @NotNull BigDecimal prix,
        @NotNull @Min(1) Integer quantite) {}
