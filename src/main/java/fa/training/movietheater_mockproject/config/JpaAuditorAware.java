package fa.training.movietheater_mockproject.config;

import fa.training.movietheater_mockproject.security.SecurityUtil;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return SecurityUtil.getCurrentUserLogin();
    }
}
