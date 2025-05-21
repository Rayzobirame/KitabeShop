package com.kitabe.notification.domaine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeEvenementRepository extends JpaRepository<CommandeEvenementEntite, Long> {

    boolean existsByEvenementId(String evenementId);
}
