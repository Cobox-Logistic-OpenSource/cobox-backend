package coboxlogistic.startup.backend.shared.infrastructure.persistence.jpa.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA Configuration for the Cobox Backend Platform.
 * Enables JPA auditing and repository scanning.
 *
 * @author Cobox Team
 * @version 1.0
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "coboxlogistic.startup.backend")
public class JpaConfig {
}