package bg.softuni.recipe.explorer.service.init;

import bg.softuni.recipe.explorer.model.entity.Role;
import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.model.enums.RoleEnum;
import bg.softuni.recipe.explorer.repository.RoleRepository;
import bg.softuni.recipe.explorer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceInitImpl {

    private static final int COUNT_BY_ROLE = 3;
    private static final String LASTNAME_SUFFIX = "ov";


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceInitImpl(
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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

    // user[1...] userov, moderator[1...] moderatorov, admin[1...] adminov
    private static User mapToUserFromRoles(Role role, int seq) {
        String roleNameBase = role.getName().name().toLowerCase();
        String nameBase = roleNameBase + seq;

//        TODO: password encoding

        return new User()
                .setUsername(nameBase)
                .setFirstName(nameBase)
                .setLastName(roleNameBase + LASTNAME_SUFFIX)
                .setEmail(nameBase + "@" + roleNameBase)
                .setPassword(nameBase)
                .setRoles(Set.of(role))
                ;
    }
}
