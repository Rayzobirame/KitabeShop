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
            this.panier.totalMontant = getPanierTotal();
        },

        loadPanier() {
            this.panier = getPanier();
        },

        updateItemQuantite(code, quantite) {
            updateProduitQuantite(code, quantite);
            this.loadPanier();
            this.panier.totalMontant = getPanierTotal();
        },

        removePanier() {
            clearPanier();
        },
        creerCommande() {
            let order = Object.assign({}, this.commandeForm, {items: this.panier.items});
            //console.log("Commande", commande);
            $.ajax ({
                url: '/api/commandes',
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                data : JSON.stringify(order),
                success: (resp) => {
                    //console.log("Commande Resp:", resp)
                    this.removePanier();
                    //alert("Commande reussie avec succes")
                    window.location = "/commandes/"+resp.commandeNum;
                }, error: (err) => {
                    console.log("Erreur lors de la creation de la commande:", err)
                    alert("Echec de la creation")
                }
            });
        }
    }));
});