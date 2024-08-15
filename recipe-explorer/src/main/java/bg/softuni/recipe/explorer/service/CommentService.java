package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.model.dto.CommentPutDTO;
import bg.softuni.recipe.explorer.model.dto.CommentViewDTO;
import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface CommentService {

    List<CommentViewDTO> getCommentsForRecipe(Long recipeId) throws JsonProcessingException;

    void approve(Long id);

    void delete(Long id, AppUserDetails appUserDetails);

    void post(String message, Long recipeId, Long authorId);

    CommentPutDTO get(Long id, AppUserDetails appUserDetails);

    void edit(CommentPutDTO dto, AppUserDetails appUserDetails);
}
