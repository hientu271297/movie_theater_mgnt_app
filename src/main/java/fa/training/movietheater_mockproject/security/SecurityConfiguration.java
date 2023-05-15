package fa.training.movietheater_mockproject.security;

import fa.training.movietheater_mockproject.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .and()
                .formLogin()
                .loginPage("/login") // Define login url
                .loginProcessingUrl("/authentication")
                .successHandler(authenticationSuccessHandler)
                .failureUrl("/login?error=true")
                .usernameParameter("email") // default username parameter = "username"
                .permitAll() // public page
        .and()
                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")))
                .authorizeRequests()
        .and()
                .rememberMe().tokenValiditySeconds(86400)
        .and()
                .authorizeRequests()
                .antMatchers("/styles/**").permitAll()
                .antMatchers("/forget-password/**").permitAll()
                .antMatchers("/reset-password/**").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/templates/fragment/head").permitAll()
                .antMatchers("/**/admin/**").hasAuthority(Role.ADMIN.toString())
                .antMatchers("/**/*.css").permitAll()
                .antMatchers("/seat/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .anyRequest()
                .authenticated()
        .and()
                .httpBasic()
        .and()
                .csrf().disable();

        return httpSecurity.build();
    }
}
