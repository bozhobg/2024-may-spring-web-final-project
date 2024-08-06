package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.constants.SortingEnum;
import bg.softuni.recipe.explorer.model.dto.*;
import bg.softuni.recipe.explorer.model.entity.Recipe;
import bg.softuni.recipe.explorer.model.enums.DietaryType;
import bg.softuni.recipe.explorer.model.enums.MealType;
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

    @Transactional
    List<RecipeShortInfoDTO> filter(MealType mealType, Long dietId, SortingEnum ratingSort);

//    TODO:
//    List<RecipeShortInfoDTO> search(String recipeNameSearch, String ingredientNameSearch, MealType mealType, DietaryType dietType);
}
