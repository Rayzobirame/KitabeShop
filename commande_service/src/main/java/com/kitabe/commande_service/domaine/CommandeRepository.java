package com.kitabe.commande_service.domaine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommandeRepository extends JpaRepository<CommandeEntite,Long> {

    /**
     * Recherche toutes les commandes ayant un statut spécifique.
     *
     * @param status Le statut des commandes à rechercher ({@link CommandeStatus}).
     * @return Une liste de commandes ({@link CommandeEntite}) correspondant au statut spécifié.
     */
    List<CommandeEntite> findByStatus(CommandeStatus status);
    /**
     * Recherche une commande par son numéro unique.
     *
     * @param commandeNum Le numéro unique de la commande à rechercher.
     * @return Un {@link Optional} contenant la commande si elle existe, ou vide sinon.
     */
    Optional<CommandeEntite> findByCommandeNum(String commandeNum);

    /**
     * Met à jour le statut d'une commande identifiée par son numéro.
     * Cette méthode recherche la commande par son numéro, met à jour son statut, et enregistre les modifications
     * dans la base de données.
     *
     * @param commandeNum Le numéro unique de la commande à mettre à jour.
     * @param status      Le nouveau statut à appliquer ({@link CommandeStatus}).
     * @throws IllegalArgumentException Si aucune commande n'est trouvée avec le numéro spécifié.
     */
    default void updateCommandeStatus(String commandeNum, CommandeStatus status) {
        CommandeEntite commande = this.findByCommandeNum(commandeNum).orElseThrow();
        commande.setStatus(status);
        this.save(commande);
    }

}
