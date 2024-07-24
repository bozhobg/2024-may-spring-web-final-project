package bg.softuni.recipe.explorer.model.dto;

import bg.softuni.recipe.explorer.constants.RegexPatterns;
import bg.softuni.recipe.explorer.validation.EmailValid;
import bg.softuni.recipe.explorer.validation.UsernameValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegisterDTO {

    @NotBlank(message = "{user.register.username.not.blank}")
    @Size(min = 3, max = 15, message = "{user.register.username.size}")
    @UsernameValid(message = "{user.register.username.invalid}")
    private String username;

    @NotBlank(message = "{user.register.first.name.not.blank}")
    @Size(min = 2, max = 20, message = "{user.register.first.name.size}")
    private String firstName;

    @NotBlank(message = "{user.register.last.name.not.blank}")
    @Size(min = 2, max = 20, message = "{user.register.last.name.size}")
    private String lastName;

    @NotBlank(message = "{user.register.email.not.blank}")
    @Email(regexp = RegexPatterns.EMAIL_REGEX, message = "{user.register.email.format}")
    @EmailValid(message = "{user.register.email.invalid}")
    private String email;

    @NotBlank(message = "{user.register.password.not.blank}")
    @Size(min = 5, max = 20, message = "{user.register.password.size}")
    private String password;

    @NotBlank(message = "{user.register.password.not.blank}")
    @Size(min = 5, max = 20, message = "{user.register.password.size}")
    private String confirmPassword;

    public UserRegisterDTO() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserRegisterDTO setFirstName( String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRegisterDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
