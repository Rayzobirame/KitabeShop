package com.kitabe.kitabe_webapp.commande;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Client(
        @NotBlank(message = "Le nom du client est requis") String nom,
        @NotBlank(message = "Le prenom du client est requis") String prenom,
        @NotBlank(message = "Le email du client est requis") @Email String email,
        @NotBlank(message = "Le numero telephone du client est requis") String telephone) {}
