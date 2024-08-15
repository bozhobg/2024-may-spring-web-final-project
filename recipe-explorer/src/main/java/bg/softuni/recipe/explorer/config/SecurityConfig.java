package bg.softuni.recipe.explorer.config;

import bg.softuni.recipe.explorer.repository.UserRepository;
import bg.softuni.recipe.explorer.service.impl.AppUserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Bean
    public UserDetailsService userDetailsService(
            UserRepository userRepository
    ) {
        return new AppUserDetailsServiceImpl(userRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers
                                        ("/", "/users/login", "/users/register", "/about", "/error"
//                                                "/api/ingredients/short" // -> how is it secured?
                                        ).permitAll()
                                .requestMatchers("/comments/*/approve"
                                ).hasAnyAuthority("ROLE_ADMIN", "ROLE_MODERATOR")
                                .requestMatchers("/users/*/grant", "/users/*/revoke").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(
                                        PathRequest.toStaticResources().atCommonLocations()
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/users/login")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                // TODO: after login /login?continue ->  404 error page?
                                .defaultSuccessUrl("/home", true)
                                .failureUrl("/users/login?error=true")
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/users/logout")
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)
                )
                .build();
    }
}
