package com.kitabe.notification.domaine.model;

import java.time.LocalDateTime;
import java.util.Set;

public record CommandeDelivrerEvenement(
        String evenementId,
        String commandeNum,
        Set<CommandeItems> items,
        Client client,
        Addresse addresse,
        LocalDateTime creerLe) {}
