package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.model.entity.Role;
import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import bg.softuni.recipe.explorer.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public class AppUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsServiceImpl(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username"));

        return mapToAppUserDetails(user);
    }

    private static AppUserDetails mapToAppUserDetails(User user) {

        AppUserDetails appUserDetails = new AppUserDetails(
                user.getUsername(),
                user.getPassword(),
                mapAuthorities(user.getRoles()),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );

        return appUserDetails;
    }

    private static List<SimpleGrantedAuthority> mapAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
                .toList();
    }
}
