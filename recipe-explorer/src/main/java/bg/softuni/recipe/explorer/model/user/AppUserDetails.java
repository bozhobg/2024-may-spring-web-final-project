package bg.softuni.recipe.explorer.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AppUserDetails extends User {

    private String firstName;
    private String lastName;
    private String email;

    public AppUserDetails(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            String firstName,
            String lastName,
            String email
    ) {
        super(username, password, authorities);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public AppUserDetails setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public AppUserDetails setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AppUserDetails setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
