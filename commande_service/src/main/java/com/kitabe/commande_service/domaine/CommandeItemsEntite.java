package com.kitabe.commande_service.domaine;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "commande_item")
public class CommandeItemsEntite {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "commande_item_id_generator")
    @SequenceGenerator(name = "commande_item_id_generator",sequenceName = "commande_item_id_seq")
    private Long id;

    private String nom;

   @Column(nullable = false)
    private String code;

   @Column(nullable = false)
   private Double prix;

   @Column(nullable = false)
   private Integer quantite;

   @ManyToOne(optional = false)
   @JoinColumn(name = "commande_id")
   private CommandeEntite commande;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public CommandeEntite getCommande() {
        return commande;
    }

    public void setCommande(CommandeEntite commande) {
        this.commande = commande;
    }
}
