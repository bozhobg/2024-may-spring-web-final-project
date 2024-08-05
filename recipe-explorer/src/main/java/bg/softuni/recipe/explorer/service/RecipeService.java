package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.model.dto.*;
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

    List<RecipeBasicDTO> getAllBasicByUser(Long userId);

    @Transactional
    RecipeDetailsDTO getDetailsById(Long id);

    @Transactional
    Long add(RecipeAddDTO dto, Long userId);

    @Transactional
    RecipeEditDTO getEditDTO(Long id);

    void put(Long recipeId, RecipeEditDTO dto);

    @Transactional
    void delete(Long id);

    void updateAvgRating(Long recipeId, BigDecimal averageRating);

}
