package com.kitabe.commande_service.domaine.model;

import java.time.LocalDateTime;
import java.util.Set;

public record CreerCommandeEvenement(
        String evenementId,
        String commandeNum,
        Set<CommandeItems> items,
        Client client,
        Addresse addresse,
        LocalDateTime creerLe
) {
}
