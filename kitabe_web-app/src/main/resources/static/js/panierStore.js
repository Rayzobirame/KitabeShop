// Clé utilisée pour stocker l'état du panier dans localStorage
const KITABESHOP_STATE_KEY = "KITABESHOP_STATE";

/**
 * Récupère l'état du panier depuis localStorage.
 * Si aucun panier n'existe, initialise un panier vide avec un tableau d'items et un total de 0.
 * @returns {Object} L'état du panier sous forme d'objet JavaScript (parsed depuis JSON)
 */
const getPanier = function () {
    let panier = localStorage.getItem(KITABESHOP_STATE_KEY);
    if (!panier) {
        panier = JSON.stringify({ item: [], totalMontant: 0 });
        localStorage.setItem(KITABESHOP_STATE_KEY, panier);
    }
    return JSON.parse(panier);
};

/**
 * Ajoute un produit au panier ou incrémente sa quantité s'il existe déjà.
 * @param {Object} produit - L'objet produit avec au moins les propriétés code, nom, prix
 */
const addProduitAuPanier = function (produit) {
    let panier = getPanier();
    let panierItem = panier.item.find((itemModel) => itemModel.code === produit.code);
    if (panierItem) {
        panierItem.quantite = parseInt(panierItem.quantite) + 1;
    } else {
        panier.item.push(Object.assign({}, produit, { quantite: 1 }));
    }
    panier.totalMontant = getPanierTotal();
    localStorage.setItem(KITABESHOP_STATE_KEY, JSON.stringify(panier));
    updatePanierItemCount();
};

/**
 * Met à jour la quantité d'un produit dans le panier.
 * Si la quantité est < 1, supprime le produit.
 * @param {number|string} code - Le code du produit à mettre à jour
 * @param {number} quantite - La nouvelle quantité
 */
const updateProduitQuantite = function (code, quantite) {
    let panier = getPanier();
    if (quantite < 1) {
        panier.item = panier.item.filter((itemModel) => itemModel.code !== code);
    } else {
        let panierItem = panier.item.find((itemModel) => itemModel.code === code);
        if (panierItem) {
            panierItem.quantite = parseInt(quantite);
        } else {
            console.log("Le code de ce produit n'est pas prêt à être placé dans une commande, ignoré.");
        }
    }
    panier.totalMontant = getPanierTotal();
    localStorage.setItem(KITABESHOP_STATE_KEY, JSON.stringify(panier));
    updatePanierItemCount();
};

/**
 * Supprime un produit spécifique du panier.
 * @param {number|string} code - Le code du produit à supprimer
 */
const deleteProduit = function (code) {
    let panier = getPanier();
    panier.item = panier.item.filter((itemModel) => itemModel.code !== code);
    panier.totalMontant = getPanierTotal();
    localStorage.setItem(KITABESHOP_STATE_KEY, JSON.stringify(panier));
    updatePanierItemCount();
};

/**
 * Vide complètement le panier.
 */
const clearPanier = function () {
    localStorage.removeItem(KITABESHOP_STATE_KEY);
    updatePanierItemCount();
};

/**
 * Met à jour l'affichage du nombre total d'items dans le panier.
 */
function updatePanierItemCount() {
    let panier = getPanier();
    let count = 0;
    panier.item.forEach((itemModel) => {
        count += itemModel.quantite;
    });
    const element = document.querySelector('#cart-item-count');
    if (element) {
        element.textContent = `(${count})`;
    }
}

/**
 * Calcule le montant total du panier en fonction des prix et quantités.
 * @returns {number} Le totalMontant
 */
function getPanierTotal() {
    let panier = getPanier();
    let totalMontant = 0;
    panier.item.forEach((item) => {
        totalMontant += item.prix * item.quantite;
    });
    return totalMontant;
}

// Initialisation avec Alpine.js
document.addEventListener('alpine:init', () => {
    Alpine.data('panier', () => ({
        panier: { item: [], totalMontant: 0 },
        commandeForm: {
            pseudo: "utilisateur",
            client: {
                nom: "Camara",
                prenom: "Birame",
                email: "Birame@gmail.com",
                telephone: "772883172"
            },
            livraisonAddresse: {
                addresse1: "Thiaroye waounde",
                addresse2: "Grand Dakar",
                addressePostal: "BP 90",
                ville: "Waounde",
                region: "Matam",
                pays: "Senegal"
            }
        },

        init() {
            updatePanierItemCount();
            this.loadPanier();
        },

        loadPanier() {
            this.panier = getPanier();
            this.panier.totalMontant = getPanierTotal();
        },

        updateItemQuantite(code, quantite) {
            updateProduitQuantite(code, quantite);
            this.loadPanier();
        },

        removeFromPanier(code) {
            deleteProduit(code);
            this.loadPanier();
        },

        removePanier() {
            clearPanier();
            this.loadPanier();
        },

        creerCommande() {
            const commandeItems = this.panier.item.map(item => ({
                code: item.code,
                nom: item.nom,
                prix: item.prix,
                quantite: item.quantite
            }));

            let commande = {
                pseudo: this.commandeForm.pseudo,
                client: this.commandeForm.client,
                livraisonAddresse: this.commandeForm.livraisonAddresse,
                items: commandeItems,
                totalMontant: this.panier.totalMontant
            };

            $.ajax({
                url: '/api/commande',
                method: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify(commande),
                success: (resp) => {
                    console.log("Réponse reçue:", resp);
                    this.removePanier();
                    window.location.href = "/commandes/" + resp.commandeNum; // Changé window.location pour window.location.href
                },
                error: (xhr, status, error) => {
                    console.error("Erreur lors de la validation de la commande:", {
                        status: status,
                        error: error,
                        responseText: xhr.responseText,
                        statusCode: xhr.status
                    });
                }
            });
        }
    }));
});