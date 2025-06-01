document.addEventListener("alpine:init", () => {
    Alpine.data('initData',() =>({
        panier:{item:[], totalMontant:0},
        commandeForm:{
            client:{
                nom : "Birame",
                email : "Birame@gmail.com",
                telephone : "772883172"
            },
            livraisonAddresse:{
                addresse1: "Thiaroye waounde",
                addresse2: "Grand Dakar",
                addressePostal:"BP 90",
                ville:"Waounde",
                region:"Matam",
                pays:"Senegal"

            }
        },
        init(){
            updatePanierItemCount();
            this.loadPanier();
            this.panier.totalMontant = getPanierTotal();
        },
        loadPanier(){
           this.panier = getPanierTotal();
        },
        updateItemQuantite(code,quantite){
            updateProduitQuantite(code,quantite);
            this.loadPanier();
            this.panier.totalMontant = getPanierTotal();
        },
        removePanier(){
            clearPanier();
        },
        creerCommande(){
            let commande = Object.assign({}, this.commandeForm,{item:[], totalMontant:0});
            $.ajax({
                url:'http://localhost:8989/commande/api/commandes',
                method:'POST',
                dataType:'json',
                contentType:'application/json',
                data: JSON.stringify(commande),
                success:(resp)=>{
                    this.removePanier();
                    //alert("La commande a été effectue avec success");
                    window.location = "/commande/"+resp.commandeNum;
                },
                error:(err)=>{
                    console.log("Impossible de valider la commande" +err);
                }
            });
        },
    }))
})