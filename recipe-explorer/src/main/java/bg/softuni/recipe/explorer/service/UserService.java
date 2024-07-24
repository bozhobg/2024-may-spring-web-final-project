package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.model.dto.UserRegisterDTO;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    boolean isUsernameUnique(String username);

    boolean isEmailUnique(String email);

    @Transactional
    void doRegister(UserRegisterDTO registerDTO);
}
