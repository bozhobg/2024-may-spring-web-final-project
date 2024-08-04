package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.model.dto.CommentRestDTO;
import bg.softuni.recipe.explorer.model.dto.CommentViewDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface CommentService {

    List<CommentViewDTO> getCommentsForRecipe(Long recipeId) throws JsonProcessingException;

    void approve(Long id);

    void delete(Long id);

    void post(String message, Long recipeId, Long authorId);

    CommentRestDTO get(Long id);

    void edit(Long id, String message, Long authorId);
}
