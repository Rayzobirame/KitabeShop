package com.kitabe.commande_service.domaine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitabe.commande_service.domaine.model.*;
import com.kitabe.commande_service.web.controlleurs.CommandeController;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service métier pour la gestion des événements de commande.
 * Cette classe gère la création, la persistance et la publication des événements liés aux commandes.
 * Elle interagit avec le repository {@link CommandeEvenementRepository} pour stocker les événements
 * et avec {@link CommandeEvenementPublisher} pour les publier via RabbitMQ.
 */
@Service
@Transactional
public class CommandeEvenementService {

    private static final Logger log = LoggerFactory.getLogger(CommandeController.class);
    private CommandeEvenementRepository commandeEvenementRepository;
    private CommandeEvenementPublisher commandeEvenementPublish;
    private ObjectMapper objectMapper;

    /**
     * Construit une instance de {@link CommandeEvenementService}.
     *
     * @param commandeEvenementRepository Le repository pour gérer la persistance des événements de commande.
     * @param commandeEvenementPublish    Le publisher pour envoyer les événements à RabbitMQ.
     * @param objectMapper                L'instance de {@link ObjectMapper} pour sérialiser/désérialiser les payloads JSON.
     */
    public CommandeEvenementService(
            CommandeEvenementRepository commandeEvenementRepository,
            CommandeEvenementPublisher commandeEvenementPublish,
            ObjectMapper objectMapper) {
        this.commandeEvenementRepository = commandeEvenementRepository;
        this.commandeEvenementPublish = commandeEvenementPublish;
        this.objectMapper = objectMapper;
    }

    /**
     * Convertit un objet en une chaîne JSON.
     * Cette méthode sérialise un objet (généralement un événement) en JSON pour le stocker comme payload.
     *
     * @param object L'objet à sérialiser en JSON.
     * @return La représentation JSON de l'objet sous forme de chaîne.
     * @throws RuntimeException Si une erreur de sérialisation JSON survient.
     */
    private String toJsonPayload(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convertit une chaîne JSON en un objet du type spécifié.
     * Cette méthode désérialise un payload JSON en un objet Java.
     *
     * @param json La chaîne JSON à désérialiser.
     * @param type La classe du type cible pour la désérialisation.
     * @param <T>  Le type générique de l'objet cible.
     * @return L'objet désérialisé.
     * @throws RuntimeException Si une erreur de désérialisation JSON survient.
     */
    public <T> T fromJsonPayload(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Publie un événement de commande en fonction de son type.
     * Cette méthode examine le type de l'événement et le publie via {@link CommandeEvenementPublisher}
     * si le type est {@link CommandeEvenementType#COMMANDE_CREER}.
     *
     * @param evenement L'entité de l'événement de commande à publier.
     */
    private void publishEvenement(CommandeEvenementEntite evenement) {
        CommandeEvenementType evenementType = evenement.getEvenementType();
        switch (evenementType) {
            case COMMANDE_CREER:
                CreerCommandeEvenement creerCommandeEvenement =
                        fromJsonPayload(evenement.getPayload(), CreerCommandeEvenement.class);
                commandeEvenementPublish.publish(creerCommandeEvenement);
                break;
            case COMMANDE_DELIVREE:
                CommandeDelivrerEvenement commandeDelivrerEvenement =
                        fromJsonPayload(evenement.getPayload(), CommandeDelivrerEvenement.class);
                commandeEvenementPublish.publish(commandeDelivrerEvenement);
                break;
            case COMMANDE_ANNULEE:
                CommandeAnnuleeEvenement commandeAnnuleeEvenement =
                        fromJsonPayload(evenement.getPayload(), CommandeAnnuleeEvenement.class);
                commandeEvenementPublish.publish(commandeAnnuleeEvenement);
                break;
            case COMMANDE_ECHEC_PROCESSUS:
                CommandeErreurEvenement commandeErreurEvenement =
                        fromJsonPayload(evenement.getPayload(), CommandeErreurEvenement.class);
                commandeEvenementPublish.publish(commandeErreurEvenement);
                break;
            default:
                log.warn("Type devenement commande inconnu:{}", evenementType);
        }
    }

    /**
     * Publie tous les événements de commande en attente et les supprime après publication.
     * Cette méthode récupère tous les événements de la table {@code commande_evenements}, les publie via RabbitMQ,
     * puis les supprime de la base de données.
     */
    public void publishCommandeOrder() {
        Sort sort = Sort.by("creerLe").ascending();
        List<CommandeEvenementEntite> even = commandeEvenementRepository.findAll(sort);
        log.info("{} evenements commande ont ete publier", even.size());
        for (CommandeEvenementEntite evenement : even) {
            this.publishEvenement(evenement);
            commandeEvenementRepository.delete(evenement);
        }
    }

    /**
     * Enregistre un événement de commande dans la base de données.
     * Cette méthode persiste un événement de commande (de n'importe quel type implémentant {@link CommandeEvenement})
     * en le transformant en une entité {@link CommandeEvenementEntite}. Elle détermine dynamiquement le type
     * d'événement ({@link CommandeEvenementType}) en fonction de l'instance de l'événement fourni.
     *
     * @param evenement L'événement de commande à enregistrer (doit implémenter {@link CommandeEvenement}).
     * @throws IllegalArgumentException Si le type d'événement n'est pas supporté.
     */
    public void save(CommandeEvenement evenement) {
        CommandeEvenementEntite commandeEvenementEntite = new CommandeEvenementEntite();
        commandeEvenementEntite.setEvenementId(evenement.evenementId());
        commandeEvenementEntite.setCommandeNum(evenement.commandeNum());
        commandeEvenementEntite.setCreerLe(evenement.creerLe());
        commandeEvenementEntite.setPayload(toJsonPayload(evenement));

        // Déterminer le type d'événement en fonction de l'instance
        if (evenement instanceof CreerCommandeEvenement) {
            commandeEvenementEntite.setEvenementType(CommandeEvenementType.COMMANDE_CREER);
        } else if (evenement instanceof CommandeDelivrerEvenement) {
            commandeEvenementEntite.setEvenementType(CommandeEvenementType.COMMANDE_DELIVREE);
        } else if (evenement instanceof CommandeAnnuleeEvenement) {
            commandeEvenementEntite.setEvenementType(CommandeEvenementType.COMMANDE_ANNULEE);
        } else if (evenement instanceof CommandeErreurEvenement) {
            commandeEvenementEntite.setEvenementType(CommandeEvenementType.COMMANDE_ECHEC_PROCESSUS);
        } else {
            throw new IllegalArgumentException(
                    "Type d'événement non supporté : " + evenement.getClass().getName());
        }

        this.commandeEvenementRepository.save(commandeEvenementEntite);
    }
}
