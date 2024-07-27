package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.model.dto.UserRegisterDTO;
import bg.softuni.recipe.explorer.model.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserService {

    boolean isUsernameUnique(String username);

    boolean isEmailUnique(String email);

    @Transactional
    void doRegister(UserRegisterDTO registerDTO);

    User getUserById(Long id);
}
