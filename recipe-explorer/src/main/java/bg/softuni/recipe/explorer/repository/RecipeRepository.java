package bg.softuni.recipe.explorer.repository;

import bg.softuni.recipe.explorer.model.entity.Diet;
import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.entity.Recipe;
import bg.softuni.recipe.explorer.model.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    boolean existsByName(String name);

    List<Recipe> findAllByAuthor_Id(Long userId);

    List<Recipe> findAllByMealTypeAndDietsContaining(MealType mealType, Diet diet);

    List<Recipe> findAllByDietsContaining(Diet diet);

    List<Recipe> findAllByMealType(MealType mealType);

    List<Recipe> findAllByIngredientsContaining(Ingredient ingredient);

     Recipe findFirstByOrderByAverageRatingDesc();

     @Query("""
             select r from Recipe r order by rand() limit 1
             """)
     Recipe getRandomRecipe();

     Recipe findTopByOrderByCreatedOnDesc();

     Optional<Recipe> findByName(String name);

//    TODO: how to make custom queries with bunch of params for DB search based on strings for recipe name, ingredient name
//    TODO: types of diet, meal
//    bullshit
//    List<Recipe> findAllByNameLikeIgnoreCaseAndIngredientsContainingAndDietsContainingAndMealType(
//            String name, Recipe ingredient, Diet diet, MealType mealType
//    );

//    bullshit
//    @Query(value = """
//            SELECT * FROM recipe_explorer.recipes r
//            JOIN recipe_explorer.recipe_ingredient ri ON r.id = ri.recipe_id
//            JOIN recipe_explorer.ingredients i ON i.id = ri.ingredient_id
//            JOIN recipe_explorer.recipe_diet rd ON rd.recipe_id = r.id
//            JOIN recipe_explorer.diets d ON d.id = rd.diet_id
//            WHERE LOWER(r.name) LIKE CONCAT('%', LOWER(:name), '%')
//            AND LOWER(i.name) LIKE CONCAT('%', LOWER(:ingName), '%')
//            AND d.dietary_type = :dietaryType
//            AND r.meal_type = :mealType
//            """,
//            nativeQuery = true
//    )
//    List<Recipe> findByAllSearchCriteria(String name, String ingName, MealType mealType, DietaryType dietaryType);
}
