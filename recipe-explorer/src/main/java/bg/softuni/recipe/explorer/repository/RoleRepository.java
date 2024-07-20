package bg.softuni.recipe.explorer.repository;

import bg.softuni.recipe.explorer.model.entity.Role;
import bg.softuni.recipe.explorer.model.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleEnum name);
}
