package com.kitabe.kitabe_webapp.commande;

import com.kitabe.kitabe_webapp.catalogue.PagedResult;
import com.kitabe.kitabe_webapp.catalogue.Produit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;
import java.util.Map;

public interface CommandeServiceClient {

    @PostExchange("/commande/api/commande")
    CommandeConfirmationDTO creerCommande(@RequestHeader Map<String,?> headers, @RequestBody CreerCommandeRequest request);

    @GetExchange("/commande/api/commandes")
    List<CommandeSommaire> getCommandes(@RequestHeader Map<String, ?> headers);

    @GetExchange("/commande/api/commande/{commandeNum}")
    CommandeDTO getCommande(@RequestHeader(required = false) Map<String, ?> headers, @PathVariable String commandeNum);
}
