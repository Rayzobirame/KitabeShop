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
        },

        loadProduit(pageNum) {
            if (pageNum < 1) pageNum = 1;
            if (pageNum > this.produits.totalPages) pageNum = this.produits.totalPages;
            fetch(`/api/produits?page=${pageNum}`)
                .then(response => response.json())
                .then(res => {
                    console.log("Reponse produits:", res);
                    this.produits = res; // res doit contenir pageNumero, totalPages, isPremier, etc.
                })
                .catch(error => console.error('Erreur lors du chargement des produits:', error));
        },

        addAuPanier(produit) {
            addProduitAuPanier(produit);
        }
    }));
});