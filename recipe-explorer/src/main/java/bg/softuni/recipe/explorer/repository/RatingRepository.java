package bg.softuni.recipe.explorer.repository;

import bg.softuni.recipe.explorer.model.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByRecipe_IdAndUser_Id(Long recipeId, Long userId);
}
