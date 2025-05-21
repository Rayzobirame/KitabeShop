package com.kitabe.commande_service.domaine;

import com.kitabe.commande_service.domaine.model.CommandeSommaire;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeRepository extends JpaRepository<CommandeEntite, Long> {

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

    /**
     * Recherche les commandes associées à un utilisateur spécifique et retourne un sommaire.
     * Cette méthode utilise une requête JPQL pour sélectionner un sommaire des commandes ({@link CommandeSommaire})
     * contenant le numéro de commande et le statut, pour un utilisateur donné identifié par son pseudo.
     *
     * @param pseudo Le pseudo de l'utilisateur dont on veut récupérer les commandes.
     * @return Une liste de sommaires de commandes ({@link CommandeSommaire}) associées à l'utilisateur.
     */
    @Query("select new com.kitabe.commande_service.domaine.model.CommandeSommaire(o.commandeNum, o.status) "
            + "from CommandeEntite o "
            + "where o.pseudo = :pseudo")
    List<CommandeSommaire> findByPseudo(String pseudo);

    /**
     * Recherche une commande par le pseudo de l'utilisateur et le numéro de commande.
     * Cette méthode utilise une requête JPQL avec un LEFT JOIN FETCH pour charger les articles associés
     * ({@link CommandeItemsEntite}) de manière efficace, évitant les problèmes de lazy loading.
     *
     * @param pseudo    Le pseudo de l'utilisateur associé à la commande (mappé au paramètre nommé :pseudo).
     * @param commandeNum Le numéro unique de la commande (mappé au paramètre nommé :commandeNum).
     * @return Un {@link Optional} contenant l'{@link CommandeEntite} si elle existe, ou vide sinon.
     */
    @Query(
            "select distinct c from CommandeEntite c left join fetch c.commandeItems where c.pseudo =:pseudo and c.commandeNum=:commandeNum")
    Optional<CommandeEntite> findByPseudoAndCommandeNum(String pseudo, String commandeNum);
}
