package bg.softuni.recipe.explorer.repository;

import bg.softuni.recipe.explorer.model.entity.Diet;
import bg.softuni.recipe.explorer.model.enums.DietaryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {

    Optional<Diet> findByDietaryType(DietaryType dietaryType);
}
