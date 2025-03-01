package com.kitabe.catalogue_service.domaine;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ProduitRepository extends JpaRepository<ProduitEntite, Long> {
    Optional<ProduitEntite> findByCode(String code);
}
