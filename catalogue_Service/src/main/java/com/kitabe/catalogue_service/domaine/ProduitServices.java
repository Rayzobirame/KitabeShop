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

    public PagedResult<Produit> getProduit(int pageNo){
        Sort sort = Sort.by("nom").ascending();
        pageNo = pageNo <=1 ? 0 : pageNo - 1;
        Pageable pageable = PageRequest.of(pageNo, applicationProperties.pageSize(),sort);
        Page<Produit> produitPage = repo.findAll(pageable).map(ProduitWrapper::toProduit);

        return new PagedResult<>(
                produitPage.getContent(),
                produitPage.getTotalElements(),
                produitPage.getTotalPages(),
                produitPage.getNumber() +1,
                produitPage.isFirst(),
                produitPage.isLast(),
                produitPage.hasNext(),
                produitPage.hasPrevious()
        );
    }
}
