document.addEventListener('alpine:init', () => {
    Alpine.data('initData', (pageNum) => ({
        pageNum: pageNum,
        produits: {
            data: [],
            pageNumero: 0,
            totalPages: 0,
            isPremier: false,
            aPrecedent: false,
            aSuivant: false,
            aDernier: false
        },
        init() {
            console.log('Numero de page', this.pageNum);
            this.loadProduit(this.pageNum);
            updatePanierItemCount();
        },
        loadProduit(pageNum) {
            $.getJSON("/api/produits?page="+pageNum, (resp)=> {
                console.log("Produits Resp:", resp)
                this.produits= resp;
            });
        },
        addAuPanier(produit) {
            addProduitAuPanier(produit);
        }
    }));
});