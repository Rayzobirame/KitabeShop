package com.kitabe.commande_service.config;

import javax.sql.DataSource;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Configuration pour ShedLock dans l'application KitabeShop.
 * Cette classe active la synchronisation des tâches planifiées avec ShedLock et configure un {@link LockProvider}
 * pour utiliser une base de données relationnelle via JDBC. ShedLock garantit qu'une tâche planifiée ne s'exécute
 * que sur une seule instance dans un environnement distribué.
 */
@Configuration
// @EnableSchedulerLock(defaultLockAtMostFor = "10m")
public class SchedulerConfig {

    /**
     * Crée un {@link LockProvider} pour ShedLock utilisant une base de données relationnelle.
     * Ce provider utilise {@link JdbcTemplate} pour interagir avec la table {@code shedlock} dans la base de données,
     * qui stocke les informations de verrouillage. La configuration utilise l'heure de la base de données pour
     * synchroniser les verrous.
     *
     * @param dataSource La source de données configurée pour l'application.
     * @return Un {@link LockProvider} basé sur {@link JdbcTemplateLockProvider}.
     */
    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(JdbcTemplateLockProvider.Configuration.builder()
                .withJdbcTemplate(new JdbcTemplate(dataSource))
                .usingDbTime() // Utilise l'heure de la base de données (compatible avec Postgres, MySQL, etc.)
                .build());
    }
}
