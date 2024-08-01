package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.model.dto.RatingDTO;

import java.math.BigDecimal;

public interface RatingService {

    void put(RatingDTO dto, Long userID);

    RatingDTO getDTOByRecipeIdAndUserId(Long recipeId, Long userId);

    BigDecimal getAverageRating(Long recipeId);
}
