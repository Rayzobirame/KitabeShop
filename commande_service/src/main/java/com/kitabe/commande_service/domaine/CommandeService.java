package com.kitabe.commande_service.domaine;

import com.kitabe.commande_service.domaine.model.CommandeMapper;
import com.kitabe.commande_service.domaine.model.CreerCommandeEvenement;
import com.kitabe.commande_service.domaine.model.CreerCommandeRequest;
import com.kitabe.commande_service.domaine.model.CreerCommandeResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
/**
 * Service métier pour la gestion des commandes dans l'application KitabeShop.
 * Cette classe est responsable de la création, de la validation et du traitement des commandes.
 * Elle interagit avec le repository {@link CommandeRepository} pour persister les commandes,
 * avec {@link CommandeEvenementService} pour publier des événements liés aux commandes,
 * et avec {@link CommandeValidateur} pour valider les requêtes de création de commande.
 * Toutes les opérations sont transactionnelles pour garantir la cohérence des données.
 */
public class CommandeService {
    private final CommandeRepository commandeRepository;
    private final CommandeEvenementService commandeEvenementService;
    private CommandeValidateur validateur;
    private CommandeEvenementService evenementService;
    private static final Logger log = LoggerFactory.getLogger(CommandeService.class);
    private static final List<String> LIST_PAYS_AUTORISER_POUR_LIVRAISON = List.of("Senegal","France","Cote d'ivoire","Etats-Unis");

    /**
     * Construit une instance de {@link CommandeService}.
     *
     * @param commandeRepository        Le repository pour gérer la persistance des commandes ({@link CommandeEntite}).
     * @param validateur                Le validateur pour vérifier les requêtes de création de commande ({@link CreerCommandeRequest}).
     * @param evenementService          Le service pour gérer les événements de commande (utilisé pour la création).
     * @param commandeEvenementService1 Le service pour gérer les événements de commande (utilisé pour les autres états comme livraison ou annulation).
     */
    public CommandeService(CommandeRepository commandeRepository, CommandeValidateur validateur, CommandeEvenementService evenementService, CommandeEvenementService commandeEvenementService1) {
        this.commandeRepository = commandeRepository;
        this.validateur = validateur;
        this.evenementService = evenementService;
        this.commandeEvenementService = commandeEvenementService1;
    }

    /**
     * Crée une nouvelle commande à partir d'une requête et publie un événement de création.
     * Cette méthode valide la requête, convertit la requête en une entité de commande, associe un utilisateur,
     * persiste la commande dans la base de données, génère un événement de création, et retourne une réponse
     * contenant le numéro de la commande créée.
     *
     * @param username            Le nom d'utilisateur (pseudo) de l'utilisateur qui crée la commande.
     * @param createCommandeRequest La requête contenant les détails de la commande à créer ({@link CreerCommandeRequest}).
     * @return Une réponse contenant le numéro de la commande créée ({@link CreerCommandeResponse}).
     */
    public CreerCommandeResponse creerCommande(String username, CreerCommandeRequest createCommandeRequest) {
        validateur.validateur(createCommandeRequest);
        CommandeEntite nouvelleCommande = CommandeMapper.convertToEntite(createCommandeRequest);
        nouvelleCommande.setPseudo(username);
        CommandeEntite savedCommande = this.commandeRepository.save(nouvelleCommande);
        log.info("Une nouvelle commande a été creer avec : " + savedCommande.getCommandeNum());
        CreerCommandeEvenement commandeCreerEvenement = CommandeEvenementMapper.buildCommandeCreerSurEvenement(savedCommande);
        evenementService.save(commandeCreerEvenement);
        return new CreerCommandeResponse(savedCommande.getCommandeNum());
    }

    /**
     * Traite les commandes ayant le statut NOUVEAU.
     * Cette méthode récupère toutes les commandes avec le statut {@link CommandeStatus#NOUVEAU},
     * les traite une par une en déterminant si elles peuvent être livrées ou annulées,
     * et met à jour leur statut en conséquence.
     */
    public void nouveauProcessusCommande(){
        List<CommandeEntite> commande = commandeRepository.findByStatus(CommandeStatus.NOUVEAU);
        log.info("nouvelle commande trouvée en cours de procedure{}",commande.size());
        for (CommandeEntite commandeEntite : commande) {
            this.process(commandeEntite);
        }
    }

    /**
     * Traite une commande individuelle pour déterminer si elle peut être livrée.
     * Si la commande peut être livrée (selon le pays de livraison), son statut est mis à jour à {@link CommandeStatus#DELIVRER}
     * et un événement de livraison est publié. Sinon, elle est annulée ({@link CommandeStatus#ANNULER})
     * et un événement d'annulation est publié. En cas d'erreur, un événement d'erreur est généré.
     *
     * @param commandes L'entité de commande ({@link CommandeEntite}) à traiter.
     */
    private void process(CommandeEntite commandes) {
        try {
            if(peutEtreDelivrer(commandes)){
                log.info("La commande {} peut etre delivrer a bon port",commandes.getCommandeNum());
                commandeRepository.updateCommandeStatus(commandes.getCommandeNum(),CommandeStatus.DELIVRER);
                commandeEvenementService.save(CommandeEvenementMapper.buildCommandeDelivrerEvenement(commandes));
            }
            else {
                log.info("Cette commande {} ne peut pas etre delivrer" ,commandes.getCommandeNum());
                commandeRepository.updateCommandeStatus(commandes.getCommandeNum(),CommandeStatus.ANNULER);
                commandeEvenementService.save(CommandeEvenementMapper.buildCommandeAnnuleeEvenement(commandes,"Livraison non autoriser sur cette localité"));
            }
        }
        catch (RuntimeException e) {
            log.error("La commande numero {} n'a pas reussi a etre effectif ", commandes.getCommandeNum(), e);
            commandeEvenementService.save(CommandeEvenementMapper.buildCommandeErreurEvenement(commandes,e.getMessage()));
        }
    }

    /**
     * Vérifie si une commande peut être livrée en fonction du pays de livraison.
     * Cette méthode compare le pays de l'adresse de livraison de la commande avec une liste de pays autorisés.
     *
     * @param commandes L'entité de commande ({@link CommandeEntite}) à vérifier.
     * @return {@code true} si le pays de livraison est autorisé, {@code false} sinon.
     */
    private boolean peutEtreDelivrer(CommandeEntite commandes) {
        return LIST_PAYS_AUTORISER_POUR_LIVRAISON.contains(commandes.getLivraisonAddresse().pays().toUpperCase());
    }
}
