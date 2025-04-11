package com.kitabe.notification.domaine;

import com.kitabe.notification.ApplicationProperties;
import com.kitabe.notification.domaine.model.CommandeAnnuleeEvenement;
import com.kitabe.notification.domaine.model.CommandeDelivrerEvenement;
import com.kitabe.notification.domaine.model.CommandeErreurEvenement;
import com.kitabe.notification.domaine.model.CreerCommandeEvenement;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service de notification pour l'envoi d'emails liés aux événements de commande.
 * Ce service utilise JavaMailSender pour envoyer des emails aux clients ou à l'équipe
 * en fonction des événements de commande (création, livraison, annulation, erreur).
 * Les emails sont actuellement envoyés en texte brut et capturés par MailHog pour les tests.
 */
@Service
public class NotificationServices {
    private static final Logger log = LoggerFactory.getLogger(NotificationServices.class);

    private final JavaMailSender mailSender;
    private final ApplicationProperties properties;

    /**
     * Constructeur du service de notification.
     *
     * @param mailSender Instance de JavaMailSender pour l'envoi d'emails.
     * @param properties Propriétés de l'application contenant les configurations (ex. supportEmail).
     */
    public NotificationServices(JavaMailSender mailSender, ApplicationProperties properties) {
        this.mailSender = mailSender;
        this.properties = properties;
    }

    /**
     * Envoie une notification par email lorsqu'une commande est créée.
     * Le message est envoyé au client avec les détails de la commande.
     *
     * @param evenement Événement de création de commande contenant les informations du client et de la commande.
     */
    public void sendCommandeCreerNotification(CreerCommandeEvenement evenement) {
        String message = """
                ==========================================================
                Notification pour commande créée
                ----------------------------------------------------------
                Cher %s,
                Votre commande numéro : %s a été créée avec succès.
                L'équipe KitabeShop.
                ==========================================================
                """.formatted(evenement.client().prenom(), evenement.commandeNum());
        log.info("Envoi d'une notification de création de commande à {} (commandeNum: {})",
                evenement.client().email(), evenement.commandeNum());
        sendEmail(evenement.client().email(), "Commande créée - KitabeShop", message);
    }

    /**
     * Envoie une notification par email lorsqu'une commande est livrée.
     * Le message est envoyé au client pour confirmer la livraison.
     *
     * @param evenement Événement de livraison de commande contenant les informations du client et de la commande.
     */
    public void sendCommandeLivrerNotification(CommandeDelivrerEvenement evenement) {
        String message = """
                ==========================================================
                Notification pour commande livrée
                ----------------------------------------------------------
                Cher %s,
                Nous vous informons que votre commande numéro : %s a été livrée
                avec succès.
                Merci pour votre confiance.
                L'équipe KitabeShop.
                ==========================================================
                """.formatted(evenement.client().prenom(), evenement.commandeNum());
        log.info("Envoi d'une notification de livraison de commande à {} (commandeNum: {})",
                evenement.client().email(), evenement.commandeNum());
        sendEmail(evenement.client().email(), "Commande livrée - KitabeShop", message);
    }

    /**
     * Envoie une notification par email lorsqu'une erreur survient lors du traitement d'une commande.
     * Le message est envoyé à l'équipe pour signaler l'échec.
     *
     * @param evenement Événement d'erreur de commande contenant les détails de l'erreur et de la commande.
     */
    public void sendCommandeErreurNotification(CommandeErreurEvenement evenement) {
        String message = """
                ==========================================================
                Notification pour échec de commande
                ----------------------------------------------------------
                Bonjour l'équipe,
                Nous vous informons que la commande numéro : %s a échoué
                pour la raison suivante : %s
                Merci.
                L'équipe KitabeShop.
                ==========================================================
                """.formatted(evenement.commandeNum(), evenement.raison());
        log.info("Envoi d'une notification d'erreur de commande à {} (commandeNum: {})",
                evenement.client().email(), evenement.commandeNum());
        sendEmail(evenement.client().email(), "Erreur de commande - KitabeShop", message);
    }

    /**
     * Envoie une notification par email lorsqu'une commande est annulée.
     * Le message est envoyé au client pour l'informer de l'annulation.
     *
     * @param evenement Événement d'annulation de commande contenant les informations du client, de la commande et de la raison.
     */
    public void sendCommandeAnnulerNotification(CommandeAnnuleeEvenement evenement) {
        String message = """
                ==========================================================
                Notification pour commande annulée
                ----------------------------------------------------------
                Cher %s,
                Nous vous informons que votre commande numéro : %s a été annulée
                pour la raison suivante : %s
                Merci pour votre confiance.
                L'équipe KitabeShop.
                ==========================================================
                """.formatted(evenement.client().prenom(), evenement.commandeNum(), evenement.raison());
        log.info("Envoi d'une notification d'annulation de commande à {} (commandeNum: {})",
                evenement.client().email(), evenement.commandeNum());
        sendEmail(evenement.client().email(), "Commande annulée - KitabeShop", message);
    }

    /**
     * Envoie un email au destinataire spécifié avec le sujet et le message donnés.
     * L'email est envoyé en texte brut avec l'encodage UTF-8 pour gérer les caractères spéciaux.
     * Les emails sont capturés par MailHog pour les tests (localhost:8025).
     *
     * @param recipient Adresse email du destinataire.
     * @param subject   Sujet de l'email.
     * @param message   Contenu de l'email (en texte brut).
     * @throws IllegalArgumentException Si l'un des paramètres (destinataire, sujet, message) est vide ou nul.
     * @throws IllegalStateException    Si l'adresse email d'expédition (supportEmail) n'est pas configurée.
     * @throws RuntimeException         Si une erreur survient lors de la création ou de l'envoi de l'email.
     */
    private void sendEmail(String recipient, String subject, String message) {
        // Validation des paramètres
        if (!StringUtils.hasText(recipient)) {
            throw new IllegalArgumentException("L'adresse email du destinataire ne peut pas être vide ou nulle");
        }
        if (!StringUtils.hasText(subject)) {
            throw new IllegalArgumentException("Le sujet de l'email ne peut pas être vide ou nul");
        }
        if (!StringUtils.hasText(message)) {
            throw new IllegalArgumentException("Le message de l'email ne peut pas être vide ou nul");
        }

        // Validation de l'adresse d'expéditeur
        String fromEmail = properties.supportEmail();
        if (!StringUtils.hasText(fromEmail)) {
            throw new IllegalStateException("L'adresse email d'expédition (supportEmail) n'est pas configurée dans les propriétés");
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message, false); // false indique que le contenu est du texte brut (pas HTML)
            mailSender.send(mimeMessage);
            log.info("Email envoyé avec succès à : {}", recipient);
        } catch (MessagingException e) {
            log.error("Erreur lors de la création de l'email pour {} : {}", recipient, e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la création de l'email : " + e.getMessage(), e);
        } catch (MailException e) {
            log.error("Erreur lors de l'envoi de l'email à {} : {}", recipient, e.getMessage(), e);
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage(), e);
        }
    }
}