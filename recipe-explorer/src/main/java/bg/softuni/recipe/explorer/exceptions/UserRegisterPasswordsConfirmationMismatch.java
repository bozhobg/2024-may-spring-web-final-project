package bg.softuni.recipe.explorer.exceptions;

public class UserRegisterPasswordsConfirmationMismatch extends IllegalArgumentException {

    public UserRegisterPasswordsConfirmationMismatch(String message) {
        super(message);
    }
}
