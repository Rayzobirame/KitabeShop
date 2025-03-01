package com.kitabe.catalogue_service.domaine;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "produits")
class ProduitEntite {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produit_id_generator")
    @SequenceGenerator(name = "produit_id_generator", sequenceName = "produit_id_seq", allocationSize = 50)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "Le code produit est necessaire") private String code;

    @Column(nullable = false)
    @NotEmpty(message = "Le nom du produit est requis") private String nom;

    @Column
    private String description;

    @Column
    private String images;

    @NotNull(message = "Le prix du produit est requis") @DecimalMin("0.1") @Column(nullable = false)
    private BigDecimal prix;

    // Constructeurs
    public ProduitEntite() {}

    public ProduitEntite(String code, String nom, String description, String images, BigDecimal prix) {
        this.code = code;
        this.nom = nom;
        this.description = description;
        this.images = images;
        this.prix = prix;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    // Equals et hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProduitEntite produit = (ProduitEntite) o;
        return Objects.equals(id, produit.id) && Objects.equals(code, produit.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code);
    }

    @Override
    public String toString() {
        return "Produit{" + "id="
                + id + ", code='"
                + code + '\'' + ", nom='"
                + nom + '\'' + ", description='"
                + description + '\'' + ", images='"
                + images + '\'' + ", prix="
                + prix + '}';
    }
}
