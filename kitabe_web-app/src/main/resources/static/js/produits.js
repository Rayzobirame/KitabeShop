document.addEventListener('alpine:init', () => {
    Alpine.data('initData', (pageNum) => ({
        pageNum: pageNum,
        produits:{
            data:[]
        },
        init() {
            console.log('Numero de page', this.pageNum);
            this.loadProduit(this.pageNum)
        },
        loadProduit(pageNum){
            $.getJSON("http://localhost:8989/catalogue/api/produits?page=" + pageNum, (res) => {
                console.log("Reponse produits:",res);
                this.produits = res;
            });
        },
    }))
});