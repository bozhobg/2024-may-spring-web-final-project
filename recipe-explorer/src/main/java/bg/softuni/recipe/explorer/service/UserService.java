package bg.softuni.recipe.explorer.service;

public interface UserService {
    boolean isUsernameUnique(String username);

    boolean isEmailUnique(String email);
}
