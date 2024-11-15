package bg.softuni.recipe.explorer.repository;

import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.enums.IngredientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    boolean existsByName(String name);

    Optional<Ingredient> findByName(String name);

    List<Ingredient> findAllByIngredientType(IngredientType ingType);

//    TODO: custom query if all ids exist, return non existing ids
}
