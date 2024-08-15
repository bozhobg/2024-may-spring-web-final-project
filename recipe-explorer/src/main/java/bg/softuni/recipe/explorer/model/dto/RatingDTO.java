package bg.softuni.recipe.explorer.model.dto;

import bg.softuni.recipe.explorer.model.enums.RatingEnum;
import bg.softuni.recipe.explorer.validation.RecipeIdValid;
import jakarta.validation.constraints.NotNull;

public class RatingDTO {

    @NotNull
    @RecipeIdValid
    private Long recipeId;

    @NotNull
    private RatingEnum rating;

    public RatingDTO() {}

    public Long getRecipeId() {
        return recipeId;
    }

    public RatingDTO setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
        return this;
    }

    public RatingEnum getRating() {
        return rating;
    }

    public RatingDTO setRating(RatingEnum rating) {
        this.rating = rating;
        return this;
    }
}
