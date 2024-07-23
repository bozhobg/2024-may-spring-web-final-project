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
public class WebSecurityConfig {

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
                                        ).permitAll()
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
                                .successForwardUrl("/home")
                                .failureUrl("/users/login")
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
