package com.kitabe.kitabe_webapp.commande;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record CreerCommandeRequest(
        @NotEmpty(message = "Les articles ne peuvent pas etre vide") Set<CommandeItems> items,
        @Valid Client client,
        @Valid Addresse livraisonAddresse) {}
