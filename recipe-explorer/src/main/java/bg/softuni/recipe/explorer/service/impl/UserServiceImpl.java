package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.constants.ExceptionMessages;
import bg.softuni.recipe.explorer.exceptions.ObjectNotFoundException;
import bg.softuni.recipe.explorer.exceptions.PersistenceException;
import bg.softuni.recipe.explorer.exceptions.UserRegisterPasswordsConfirmationMismatch;
import bg.softuni.recipe.explorer.model.dto.UserInfoDTO;
import bg.softuni.recipe.explorer.model.dto.UserRegisterDTO;
import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.model.enums.RoleEnum;
import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import bg.softuni.recipe.explorer.repository.RoleRepository;
import bg.softuni.recipe.explorer.repository.UserRepository;
import bg.softuni.recipe.explorer.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            ModelMapper modelMapper
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean isUsernameUnique(String username) {
        return !this.userRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailUnique(String email) {
        return !this.userRepository.existsByEmail(email);
    }

    @Override
    public void doRegister(UserRegisterDTO registerDTO) {

        if (!arePasswordsMatching(registerDTO)) {
            throw new UserRegisterPasswordsConfirmationMismatch("Passwords must match!");
        }

        User newUser = mapRegisterDataToEntity(registerDTO);

        this.userRepository.save(newUser);
    }

    @Override
    public User getUserById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(ExceptionMessages.USER_NOT_FOUND));
    }

    @Override
    public UserInfoDTO getProfileDataByUsername(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException(ExceptionMessages.USER_NOT_FOUND));

        UserInfoDTO userData = modelMapper.map(user, UserInfoDTO.class);

        return userData;
    }

    @Override
    public void patchUsername(String username, AppUserDetails appUserDetails) {

        User user = this.userRepository.findByUsername(appUserDetails.getUsername())
                .orElseThrow(() -> new ObjectNotFoundException(ExceptionMessages.USER_NOT_FOUND));

        this.userRepository.save(user.setUsername(username));
        
    }

    private User mapRegisterDataToEntity(UserRegisterDTO dto) {

        User map = this.modelMapper.map(dto, User.class);
        map.setPassword(passwordEncoder.encode(dto.getPassword()));
        map.setRoles(Set.of(
                this.roleRepository.findByName(RoleEnum.USER)
                        .orElseThrow(() -> new PersistenceException(ExceptionMessages.ROLE_INVALID))
        ));

        return map;
    }

    private static boolean arePasswordsMatching(UserRegisterDTO dto) {

        String password = dto.getPassword();
        String confirm = dto.getConfirmPassword();

        return password != null && password.equals(confirm);
    }
}
