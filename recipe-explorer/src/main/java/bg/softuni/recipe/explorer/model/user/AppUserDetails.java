package bg.softuni.recipe.explorer.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AppUserDetails extends User {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    public AppUserDetails(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            Long id,
            String firstName,
            String lastName,
            String email
    ) {
        super(username, password, authorities);
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public boolean isAdmin() {
        return super.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

    public boolean isModerator() {
        return super.getAuthorities().stream()
                .anyMatch(a -> "ROLE_MODERATOR".equals(a.getAuthority()));
    }
}
