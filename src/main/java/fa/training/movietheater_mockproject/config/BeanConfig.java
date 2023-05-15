package fa.training.movietheater_mockproject.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableJpaAuditing(auditorAwareRef = "jpaAuditorAware")
public class BeanConfig {

}
