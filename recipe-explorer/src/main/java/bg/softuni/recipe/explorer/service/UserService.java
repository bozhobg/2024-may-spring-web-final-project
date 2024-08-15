package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.model.dto.RoleDTO;
import bg.softuni.recipe.explorer.model.dto.UserInfoDTO;
import bg.softuni.recipe.explorer.model.dto.UserRegisterDTO;
import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.model.enums.RoleEnum;
import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    boolean isUsernameUnique(String username);

    boolean isEmailUnique(String email);

    @Transactional
    void doRegister(UserRegisterDTO registerDTO);

    User getUserById(Long id);

    @Transactional
    UserInfoDTO getProfileDataByUsername(String username);

    void patchUsername(String username, AppUserDetails appUserDetails);

    @Transactional
    boolean grantRole(String username, RoleEnum roleName);

    @Transactional
    boolean revokeRole(String username, RoleEnum name);
}
