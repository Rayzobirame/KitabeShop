package com.kitabe.commande_service.domaine;

import com.kitabe.commande_service.domaine.model.Addresse;
import com.kitabe.commande_service.domaine.model.Client;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "commandes")
public class CommandeEntite {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "commande_id_generator")
    @SequenceGenerator(name = "commande_id_generator",sequenceName = "commande_id_seq")
    private Long id;

    @Column(nullable = false)
    private String commandeNum;
    @Column(name="pseudo",nullable = false)
    private String pseudo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commande")
    private Set<CommandeItemsEntite> commandeItems;

    @Embedded
            @AttributeOverrides(
                    value = {
                            @AttributeOverride(name = "nom",column = @Column(name = "client_nom")),
                            @AttributeOverride(name = "prenom", column = @Column(name = "client_prenom")),
                            @AttributeOverride(name = "telephone",column = @Column(name = "client_phone")),
                            @AttributeOverride(name = "email",column = @Column(name = "client_email"))
                    }
            )
            private Client client;

    @Embedded
    @AttributeOverrides(
            value = {
                    @AttributeOverride(name = "addresse1",column = @Column(name = "livraison_addresse1")),
                    @AttributeOverride(name = "addresse2",column = @Column(name = "livraison_addresse2")),
                    @AttributeOverride(name = "addressePostal",column = @Column(name = "livraison_addresse_postal")),
                    @AttributeOverride(name = "region",column = @Column(name = "livraison_region")),
                    @AttributeOverride(name = "pays",column = @Column(name = "livraison_pays")),
                    @AttributeOverride(name = "ville",column = @Column(name = "livraison_ville")),
            }
    )
    private Addresse addresseLivraison;

    @Enumerated(EnumType.STRING)
    private CommandeStatus status;

    private String commentaire;

    @Column(name= "date_creation", nullable = false, updatable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "date_modification")
    private LocalDateTime modificationDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommandeNum() {
        return commandeNum;
    }

    public void setCommandeNum(String commandeNum) {
        this.commandeNum = commandeNum;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public Set<CommandeItemsEntite> getCommandeItems() {
        return commandeItems;
    }

    public void setCommandeItems(Set<CommandeItemsEntite> commandeItems) {
        this.commandeItems = commandeItems;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Addresse getAddresseLivraison() {
        return addresseLivraison;
    }

    public void setAddresseLivraison(Addresse addresseLivraison) {
        this.addresseLivraison = addresseLivraison;
    }

    public CommandeStatus getStatus() {
        return status;
    }

    public void setStatus(CommandeStatus status) {
        this.status = status;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }
}
