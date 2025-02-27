package com.kitabe.catalogue_service.domaine;

import com.kitabe.catalogue_service.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProduitServices {
    private final ProduitRepository repo;
    private final ApplicationProperties applicationProperties;
    public ProduitServices(ProduitRepository produitRepository, ApplicationProperties applicationProperties) {
        this.repo = produitRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Récupère une liste paginée de produits triés par nom.
     * @param pageNo Numéro de la page demandée (commence à 1 pour l'utilisateur).
     * @return PagedResult contenant les produits et les métadonnées de pagination.
     */
    public PagedResult<Produit> getProduit(int pageNo){
        //Crée un objet Sort qui définit un tri par ordre croissant (ascending) sur le champ nom par ordre alphabetique
        Sort sort = Sort.by("nom").ascending();
        // Ajuste pageNo : utilisateur commence à 1, Spring à 0
        pageNo = pageNo <=1 ? 0 : pageNo - 1;
        // Crée un objet Pageable pour la pagination et le tri
        Pageable pageable = PageRequest.of(pageNo, applicationProperties.pageSize(),sort);
        // Récupère une page de produits depuis le repository et les mappe en objets Produit
        Page<Produit> produitPage = repo.findAll(pageable).map(ProduitWrapper::toProduit);
        // Retourne un résultat paginé avec les données et métadonnées
        return new PagedResult<>(
                produitPage.getContent(),
                produitPage.getTotalElements(),
                produitPage.getTotalPages(),
                produitPage.getNumber() + 1,
                produitPage.isFirst(),
                produitPage.isLast(),
                produitPage.hasNext(),
                produitPage.hasPrevious()
        );
    }
}
