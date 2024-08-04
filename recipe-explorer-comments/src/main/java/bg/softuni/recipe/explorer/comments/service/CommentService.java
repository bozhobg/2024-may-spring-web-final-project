package bg.softuni.recipe.explorer.comments.service;

import bg.softuni.recipe.explorer.comments.model.dto.CommentAddDTO;
import bg.softuni.recipe.explorer.comments.model.dto.CommentEditDTO;
import bg.softuni.recipe.explorer.comments.model.dto.CommentViewDTO;

import java.util.List;

public interface CommentService {
    CommentViewDTO get(Long id);

    List<CommentViewDTO> getAllForRecipe(Long recipeId);

    CommentViewDTO add(CommentAddDTO addDTO, Long recipeId);

    CommentViewDTO approve(Long id);

    CommentViewDTO edit(CommentEditDTO editDTO, Long commentId);

    void delete(Long id);
}
