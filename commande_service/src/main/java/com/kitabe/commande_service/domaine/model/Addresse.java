package com.kitabe.commande_service.domaine.model;

import jakarta.validation.constraints.NotBlank;

public record Addresse(
        @NotBlank(message = "L'adresse 1 du client est requis") String addresse1,String addresse2,
        @NotBlank(message = "La ville du client est requis") String ville,
        @NotBlank(message = "La region du client est requis") String region,
        @NotBlank(message = "Le pays du client est requis") String pays,
        @NotBlank(message = "L'adresse postal du client est requis") String addressePostal

) {
}
