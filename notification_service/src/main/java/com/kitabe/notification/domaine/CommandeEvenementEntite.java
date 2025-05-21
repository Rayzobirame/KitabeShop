package com.kitabe.notification.domaine;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "commande_evenements")
public class CommandeEvenementEntite {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commande_evenement_id_generator")
    @SequenceGenerator(name = "commande_evenement_id_generator", sequenceName = "commande_evenements_id_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    private String evenementId;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creerLe = LocalDateTime.now();

    @Column(name = "date_modification")
    private LocalDateTime miseAjour;

    public CommandeEvenementEntite(String evenementId) {
        this.evenementId = evenementId;
    }

    public CommandeEvenementEntite() {}

    public LocalDateTime getMiseAjour() {
        return miseAjour;
    }

    public void setMiseAjour(LocalDateTime miseAjour) {
        this.miseAjour = miseAjour;
    }

    public LocalDateTime getCreerLe() {
        return creerLe;
    }

    public void setCreerLe(LocalDateTime creerLe) {
        this.creerLe = creerLe;
    }

    public String getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(String evenementId) {
        this.evenementId = evenementId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
