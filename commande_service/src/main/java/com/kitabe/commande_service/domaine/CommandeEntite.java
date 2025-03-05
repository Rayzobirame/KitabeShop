package com.kitabe.commande_service.domaine;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "commandes")
public class CommandeEntite {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "commande_id_generator")
    Long id;

    String commandeNum;
    String pseudo;
    String prenom;
    String nom;
    String adresse1;
    String adresse2;
    String telephone;
    String region;
    String email;
    String adressePostal;
    String ville;
    String status;
    String commentaire;
    Date creationDate;
    Date modificationDate;


}
