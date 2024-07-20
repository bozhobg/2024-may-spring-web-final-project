package bg.softuni.recipe.explorer.service.init;

import bg.softuni.recipe.explorer.model.entity.Role;
import bg.softuni.recipe.explorer.model.enums.RoleEnum;
import bg.softuni.recipe.explorer.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RoleServiceInitImpl {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceInitImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void init() {
        if (roleRepository.count() > 0) return;

        List<Role> newRoles = Arrays.stream(RoleEnum.values())
                .map(RoleServiceInitImpl::mapEnumToNewEntity)
                .toList();

        this.roleRepository.saveAll(newRoles);
    }

    private static Role mapEnumToNewEntity(RoleEnum name) {
        return new Role().setName(name);
    }
}
