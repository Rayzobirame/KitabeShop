document.addEventListener('alpine:init', () => {
    Alpine.data('initData',() =>({
        commandes:[],
        init(){
            this.loadCommandes();
            updatePanierItemCount();
        },
        loadCommandes(){
            $.getJSON("/api/commandes", (data) => {
                this.commandes = data;
            });
        },
    }))
});