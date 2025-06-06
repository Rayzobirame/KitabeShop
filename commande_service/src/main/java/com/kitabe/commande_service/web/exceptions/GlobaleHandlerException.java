package com.kitabe.commande_service.web.exceptions;

import com.kitabe.commande_service.domaine.CommandeNotFoundException;
import com.kitabe.commande_service.domaine.model.InvalideCommandeException;
import jakarta.annotation.Nullable;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/*La classe GlobaleExeptionHandler est annotée avec @RestControllerAdvice, ce qui en fait un gestionnaire
 global d’exceptions pour tous les contrôleurs de  l'application. Elle intercepte les exceptions lancées
  et les transforme en réponses HTTP bien formatées.
Le but est d’assurer que les erreurs (comme un produit non trouvé ou une erreur inattendue) soient renvoyées
 au client de manière standardisée et professionnelle, plutôt que de laisser Spring générer des réponses
  par défaut peu lisibles.*/
@RestControllerAdvice
public class GlobaleHandlerException extends ResponseEntityExceptionHandler {
    public static final URI NOT_FOUND_TYPE = URI.create("https://ap.kitabeshop.com/errors/not-found");
    public static final URI BAD_REQUEST_TYPE = URI.create("https://ap.kitabeshop.com/errors/bad-request");
    public static final URI ISE_FOUND_TYPE = URI.create("https://ap.kitabeshop.com/errors/server-error");
    public static final String SERVICE_NAME = "commande_service";

    /*Cette méthode est un "filet de sécurité" qui capture toutes les exceptions non gérées
    spécifiquement ailleurs dans ton application (par exemple, une NullPointerException,
     une erreur de base de données, etc.). Elle est invoquée grâce à @ExceptionHandler
      sans type spécifique, ce qui la rend générique.*/

    /*Pourquoi en a-t-on besoin ?

    Gestion des imprévus : Toutes les applications peuvent rencontrer des erreurs
    inattendues (bugs, pannes de base de données, etc.). Sans ce gestionnaire, Spring renverrait une réponse
    par défaut moche et peu informative.
    Robustesse : Cela garantit que même en cas d’erreur grave, le client reçoit une réponse cohérente
     et exploitable, plutôt qu’un crash brut ou une page blanche.
    Monitoring : Les champs comme timestamp et service aident à tracer et diagnostiquer les problèmes
     dans les logs ou un outil de monitoring.*/
    @ExceptionHandler
    ProblemDetail handleUnhandledException(Exception ex) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setTitle("Erreur du serveur interne");
        problemDetail.setType(ISE_FOUND_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("erreur_categorie", "generic");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /*Cette méthode est invoquée automatiquement chaque fois qu’une exception de type ProduitNotFoundException est lancée dans
    l'application (par exemple, dans le contrôleur via orElseThrow(() -> ProduitNotFoundException.forCode(code)))*/
    /*un objet ProblemDetail (une classe standard de Spring introduite dans Spring Boot 3 pour représenter des
    erreurs au format RFC 7807) Retourne cet objet ProblemDetail avec les
    informations comme type , titre , nom du service concerné.., que Spring sérialise automatiquement en JSON dans la réponse HTTP*/
    @ExceptionHandler(CommandeNotFoundException.class)
    ProblemDetail handleProduitNotFoundException(CommandeNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Commande non retrouvé");
        problemDetail.setType(NOT_FOUND_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("erreur_categorie", "generic");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(InvalideCommandeException.class)
    ProblemDetail handleCommandeNotValidException(InvalideCommandeException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("La requete pour cette commande est invalide");
        problemDetail.setType(BAD_REQUEST_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("erreur_categorie", "generic");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @Nullable @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Requete payload invalide");
        problemDetail.setTitle("Bad Request");
        problemDetail.setType(BAD_REQUEST_TYPE);
        problemDetail.setProperty("erreur", errors);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("erreur_categorie", "Validation");
        problemDetail.setProperty("timestamp", Instant.now());
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }
}
