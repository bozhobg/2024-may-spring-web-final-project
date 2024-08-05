package bg.softuni.recipe.explorer.repository;

import bg.softuni.recipe.explorer.model.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    boolean existsByName(String name);

    List<Recipe> findAllByAuthor_Id(Long userId);
}
