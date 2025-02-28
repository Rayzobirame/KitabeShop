package com.kitabe.catalogue_service.domaine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ProduitRepository extends JpaRepository<ProduitEntite,Long> {
    Optional<ProduitEntite> findByCode(String code);

}
