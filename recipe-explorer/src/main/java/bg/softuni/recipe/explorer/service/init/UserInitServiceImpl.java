package bg.softuni.recipe.explorer.service.init;

import bg.softuni.recipe.explorer.exceptions.ObjectNotFoundException;
import bg.softuni.recipe.explorer.model.entity.Role;
import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.repository.RoleRepository;
import bg.softuni.recipe.explorer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class UserInitServiceImpl {

    private static final int COUNT_BY_ROLE = 3;
    private static final String LASTNAME_SUFFIX = "ov";


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserInitServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void init() {
        if (this.userRepository.count() > 0) return;

        List<Role> roles = this.roleRepository.findAll();
        List<User> newUsers = new ArrayList<>();

        for (Role role : roles) {
            for (int i = 1; i <= COUNT_BY_ROLE; i++) {
                newUsers.add(mapToUserFromRoles(role, i));
            }
        }

        this.userRepository.saveAll(newUsers);
    }

    User getRandomUser() {

        Random random = new Random();
        long userIndexBound = this.userRepository.count();
        long randomUserId = random.nextLong(userIndexBound) + 1;

        return this.userRepository.findById(randomUserId)
                .orElseThrow(() -> new ObjectNotFoundException("Invalid random user during DB init!"));
    }

    // user[1...] userov, moderator[1...] moderatorov, admin[1...] adminov
    private User mapToUserFromRoles(Role role, int seq) {
        String roleNameBase = role.getName().name().toLowerCase();
        String nameBase = roleNameBase + seq;

        return new User()
                .setUsername(nameBase)
                .setFirstName(nameBase + "name")
                .setLastName(roleNameBase + LASTNAME_SUFFIX + seq)
                .setEmail(nameBase + "@" + roleNameBase + ".com")
                .setPassword(passwordEncoder.encode(nameBase))
                .setRoles(Set.of(role))
                ;
    }
}
