package bg.softuni.recipe.explorer.comments.repository;

import bg.softuni.recipe.explorer.comments.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByRecipeId(Long recipeId);
}
