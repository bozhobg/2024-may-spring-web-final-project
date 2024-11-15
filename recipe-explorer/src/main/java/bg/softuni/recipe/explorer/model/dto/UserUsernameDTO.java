package bg.softuni.recipe.explorer.model.dto;

import bg.softuni.recipe.explorer.validation.UsernameValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserUsernameDTO {

    @NotBlank(message = "{user.register.username.not.blank}")
    @Size(min = 3, max = 15, message = "{user.register.username.size}")
    @UsernameValid(message = "{user.register.username.invalid}")
    private String username;

    public UserUsernameDTO() {}

    public String getUsername() {
        return username;
    }

    public UserUsernameDTO setUsername(String username) {
        this.username = username;
        return this;
    }
}
