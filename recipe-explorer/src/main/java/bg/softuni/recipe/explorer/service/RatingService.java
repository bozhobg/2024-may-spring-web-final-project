package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.model.dto.RatingDTO;

public interface RatingService {

    void put(RatingDTO dto, Long userID);

    RatingDTO getDTOByRecipeIdAndUserId(Long recipeId, Long userId);
}
