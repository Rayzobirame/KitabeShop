-- V1__creer_table_commande.sql
-- Création des séquences
CREATE SEQUENCE commande_id_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE commande_item_id_seq START WITH 1 INCREMENT BY 50;

-- Création de la table commandes
CREATE TABLE commandes (
                           id BIGINT DEFAULT nextval('commande_id_seq') NOT NULL,
                           commande_num TEXT NOT NULL UNIQUE,
                           pseudo TEXT,
                           client_prenom TEXT NOT NULL,
                           client_nom TEXT NOT NULL,
                           client_phone TEXT,
                           client_email TEXT NOT NULL,
                           livraison_addresse1 TEXT NOT NULL,
                           livraison_addresse2 TEXT,
                            livraison_addresse_postal TEXT NOT NULL ,
                           livraison_region TEXT,
                           livraison_pays TEXT NOT NULL,
                           livraison_ville TEXT NOT NULL,
                           commentaire TEXT,
                           status TEXT NOT NULL,
                           date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (id)
);


create table commande_item(
    id             bigint DEFAULT nextval('commande_item_id_seq') NOT NULL,
    nom             text NOT NULL,
    code            text NOT NULL,
    prix            numeric NOT NULL,
    quantite        numeric NOT NULL,
    primary key (id),
    commande_id       bigint not null references commandes(id)
);

-- Index pour améliorer les recherches
CREATE INDEX idx_commandes_commande_id ON commandes(id);
CREATE INDEX idx_commandes_client_email ON commandes(client_email);
CREATE INDEX idx_commande_item_commande_id ON commande_item(commande_id);
