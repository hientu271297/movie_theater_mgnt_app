package fa.training.movietheater_mockproject.security;

import fa.training.movietheater_mockproject.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.nio.file.AccessDeniedException;
import java.util.Objects;
import java.util.Optional;

public final class SecurityUtil {

    public static Optional<String> getCurrentUserLogin(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (Objects.isNull(authentication)){
            return Optional.empty();
        }
        if (authentication.getPrincipal() instanceof UserDetails){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return Optional.of(userDetails.getUsername());
        }
        String email = (String) authentication.getPrincipal();
        return Optional.of(email);
    }

    public static Role getCurrentUserRole() throws AccessDeniedException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if(Objects.isNull(authentication) || CollectionUtils.isEmpty(authentication.getAuthorities())){
            throw new AccessDeniedException("Can not get Role of user");
        }

        // List<GrantedAuthority>
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .findFirst()
                .orElseThrow(()-> new AccessDeniedException("Can not get role of user"));
    }
}
