package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.model.dto.RecipeAddDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeDetailsDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeShortInfoDTO;
import bg.softuni.recipe.explorer.model.entity.Recipe;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface RecipeService {

    boolean isIdValid(Long id);

    boolean isNameUnique(String name);

    Recipe getById(Long id);

    @Transactional
    List<RecipeShortInfoDTO> getAllShort();

    @Transactional
    RecipeDetailsDTO getDetailsById(Long id);

    @Transactional
    Long add(RecipeAddDTO dto, Long userId);

    void updateAvgRating(Long recipeId, BigDecimal averageRating);
}
