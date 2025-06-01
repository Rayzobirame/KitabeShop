document.addEventListener('alpine:init', () => {
    Alpine.data('initData',(commandeNum) =>({
        commandeNum: commandeNum,
        commandeDetails:{
            items:[],
            client:{},
            livraisonAddresse: {}
        },
        init(){
            updatePanierItemCount();
            this.getCommandeDetails(this.commandeNum);
        },
        getCommandeDetails(commandeNum){
            $.getJSON("http://localhost:8989/commandes/api/commande/"+commandeNum,(data));
            //console.log("recuperer la reponse:",data);
            this.commandeDetails=data;
        },
    }))
});