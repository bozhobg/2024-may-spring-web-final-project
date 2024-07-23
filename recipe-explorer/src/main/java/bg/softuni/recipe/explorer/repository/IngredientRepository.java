package bg.softuni.recipe.explorer.repository;

import bg.softuni.recipe.explorer.model.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    boolean existsByName(String name);
}
