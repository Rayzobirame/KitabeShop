// Écoute l'événement d'initialisation d'Alpine.js
document.addEventListener('alpine:init', () => {
    // Définit un composant Alpine.js nommé 'initData'
    // Prend un paramètre commandeNum pour identifier la commande
    Alpine.data('initData', (commandeNum) => ({
        // État initial
        commandeNum: commandeNum, // Numéro de la commande
        commandeDetails: {
            items: [], // Liste des items de la commande
            client: {}, // Informations du client
            livraisonAddresse: {} // Adresse de livraison
        },
        error: null, // Ajouté pour gérer les erreurs
        totalMontant: 0, // Ajouté pour calculer le total

        // Fonction appelée au chargement du composant
        init() {
            updatePanierItemCount(); // Met à jour le compteur du panier
            this.getCommandeDetails(this.commandeNum); // Charge les détails de la commande
        },

        // Charge les détails de la commande depuis l'API
        getCommandeDetails(commandeNum) {
            $.getJSON("/api/commandes/"+ commandeNum, (data) => {
                //console.log("Recuperer reponse commande:", data)
                this.commandeDetails = data
            });
        },
        calculateTotal() {
            if (!this.commandeDetails.items || this.commandeDetails.items.length === 0) {
                return 0;
            }
            return this.commandeDetails.items.reduce((total, item) => {
                const prix = Number(item.prix) || 0;
                const quantite = Number(item.quantite) || 0;
                return total + (prix * quantite);
            }, 0);
        }
    }));
});