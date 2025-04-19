CREATE SEQUENCE commande_evenements_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE commande_evenements(
                                    id             bigint DEFAULT nextval('commande_evenements_id_seq') NOT NULL,
                                    evenement_id            text      NOT NULL unique ,
                                    creation_date              timestamp  NOT NULL ,
                                    date_modification              timestamp,
                                    primary key (id)
);
