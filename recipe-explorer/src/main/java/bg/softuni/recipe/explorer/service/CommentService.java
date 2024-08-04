package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.model.dto.CommentViewDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface CommentService {

    List<CommentViewDTO> getCommentsForRecipe(Long recipeId) throws JsonProcessingException;
}
