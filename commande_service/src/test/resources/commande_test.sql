-- V2__insert_test_data.sql
-- Script Flyway pour insérer des données de test dans les tables commandes et commande_item.

-- Assurez-vous que les séquences existent (elles sont définies dans CommandeEntite et CommandeItemsEntite)
-- Si elles n'existent pas encore, vous pouvez les créer avec :
-- ALTER SEQUENCE commande_id_seq RESTART WITH 100;
-- ALTER SEQUENCE commande_item_id_seq RESTART WITH 100;
TRUNCATE TABLE commandes CASCADE;
ALTER SEQUENCE commande_id_seq RESTART WITH  100;
ALTER SEQUENCE commande_item_id_seq RESTART WITH 100;

-- Insertion de 4 commandes dans la table commandes
INSERT INTO commandes (
    id, commande_num, pseudo,
    client_nom, client_prenom, client_phone, client_email,
    livraison_addresse1, livraison_addresse2, livraison_addresse_postal, livraison_region, livraison_pays, livraison_ville,
    status, commentaire, date_creation, date_modification
) VALUES
      -- Commande 1 : Utilisateur "alice", statut NOUVEAU, livraison au Sénégal
      (1, '364ca8b2-5255-4a2b-b042-341dae1fa617', 'user',
       'Dupont', 'Alice', '1234567890', 'alice.dupont@email.com',
       '123 Rue Principale', NULL, '12345', 'Dakar', 'Senegal', 'Dakar',
       'NOUVEAU', 'Commande en attente de traitement', '2025-04-01 10:00:00', NULL),

      -- Commande 2 : Utilisateur "bob", statut DELIVRER, livraison en France
      (2, 'a1b2c3d4-1234-5678-9012-abcdef123456', 'user',
       'Martin', 'Bob', '0987654321', 'bob.martin@email.com',
       '45 Avenue des Champs', 'Appartement 3B', '75001', 'Île-de-France', 'France', 'Paris',
       'DELIVRER', 'Commande livrée avec succès', '2025-04-02 14:30:00', '2025-04-03 09:00:00'),

      -- Commande 3 : Utilisateur "charlie", statut ANNULER, livraison aux États-Unis
      (3, 'e5f6g7h8-9876-5432-1098-fedcba654321', 'user',
       'Smith', 'Charlie', '5551234567', 'charlie.smith@email.com',
       '789 Main Street', NULL, '90210', 'California', 'Etats-Unis', 'Los Angeles',
       'ANNULER', 'Commande annulée : adresse non desservie', '2025-04-02 16:00:00', '2025-04-03 10:00:00'),

      -- Commande 4 : Utilisateur "alice", statut NOUVEAU, livraison en Côte d'Ivoire
      (4, '9a8b7c6d-4567-8901-2345-6789abcdef12', 'user',
       'Dupont', 'Alice', '1234567890', 'alice.dupont@email.com',
       '56 Boulevard Central', NULL, 'BP123', 'Abidjan', 'Cote d''ivoire', 'Abidjan',
       'NOUVEAU', NULL, '2025-04-03 08:00:00', NULL);

-- Insertion des articles associés dans la table commande_item
INSERT INTO commande_item (
    id, nom, code, prix, quantite, commande_id
) VALUES
      -- Articles pour la Commande 1 (id=1)
      (1, 'Livre Java', 'LIVRE001', 29.99, 2, 1),
      (2, 'Cahier A4', 'CAHIER001', 5.50, 5, 1),

      -- Articles pour la Commande 2 (id=2)
      (3, 'Stylo Bic', 'STYLO001', 1.20, 10, 2),
      (4, 'Agenda 2025', 'AGENDA001', 15.00, 1, 2),

      -- Articles pour la Commande 3 (id=3)
      (5, 'Calculatrice', 'CALC001', 19.99, 1, 3),

      -- Articles pour la Commande 4 (id=4)
      (6, 'Livre Python', 'LIVRE002', 34.99, 1, 4),
      (7, 'Crayons de couleur', 'CRAYON001', 8.75, 3, 4);