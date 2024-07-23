package bg.softuni.recipe.explorer.model.dto;

import bg.softuni.recipe.explorer.constants.ErrorMessages;
import bg.softuni.recipe.explorer.validation.EmailValid;
import bg.softuni.recipe.explorer.validation.UsernameValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegisterDTO {

    @NotBlank(message = ErrorMessages.FIELD_NOT_BLANK)
    @Size(min = 2, message = ErrorMessages.LENGTH_MIN_TWO)
    @UsernameValid(message = ErrorMessages.USERNAME_INVALID)
    private String username;

    @NotBlank(message = ErrorMessages.FIELD_NOT_BLANK)
    @Size(min = 2, message = ErrorMessages.LENGTH_MIN_TWO)
    private String firstName;

    @NotBlank(message = ErrorMessages.FIELD_NOT_BLANK)
    @Size(min = 2, message = ErrorMessages.LENGTH_MIN_TWO)
    private String lastName;

    @NotBlank(message = ErrorMessages.FIELD_NOT_BLANK)
    @Email(message = ErrorMessages.EMAIL_FORMAT)
    @EmailValid(message = ErrorMessages.EMAIL_INVALID)
    private String email;

    @NotBlank(message = ErrorMessages.FIELD_NOT_BLANK)
    @Size(min = 2, message = ErrorMessages.LENGTH_MIN_TWO)
    private String password;

    @NotBlank(message = ErrorMessages.FIELD_NOT_BLANK)
    @Size(min = 2, message = ErrorMessages.LENGTH_MIN_TWO)
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
