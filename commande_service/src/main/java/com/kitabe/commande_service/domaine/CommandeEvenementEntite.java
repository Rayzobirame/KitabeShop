package com.kitabe.commande_service.domaine;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "commande_evenements")
public class CommandeEvenementEntite {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commande_evenement_id_generator")
    @SequenceGenerator(name = "commande_evenement_id_generator", sequenceName = "commande_evenements_id_seq")
    private Long id;

    @Column(name = "commande_num", nullable = false)
    private String commandeNum;

    @Column(nullable = false, unique = true)
    private String evenementId;

    @Enumerated(EnumType.STRING)
    private CommandeEvenementType evenementType;

    @Column(nullable = false)
    private String payload;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creerLe = LocalDateTime.now();

    @Column(name = "date_modification")
    private LocalDateTime miseAjour;

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

    public String getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(String evenementId) {
        this.evenementId = evenementId;
    }

    public CommandeEvenementType getEvenementType() {
        return evenementType;
    }

    public void setEvenementType(CommandeEvenementType evenementType) {
        this.evenementType = evenementType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public LocalDateTime getCreerLe() {
        return creerLe;
    }

    public void setCreerLe(LocalDateTime creerLe) {
        this.creerLe = creerLe;
    }

    public LocalDateTime getMiseAjour() {
        return miseAjour;
    }

    public void setMiseAjour(LocalDateTime miseAjour) {
        this.miseAjour = miseAjour;
    }
}
