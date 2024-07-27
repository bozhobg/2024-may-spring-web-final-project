package bg.softuni.recipe.explorer.repository;

import bg.softuni.recipe.explorer.model.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    boolean existsByName(String name);

//    TODO: custom query if all ids exist, return non existing ids
}
