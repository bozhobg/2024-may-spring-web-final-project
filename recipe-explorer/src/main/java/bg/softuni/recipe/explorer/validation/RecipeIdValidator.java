package bg.softuni.recipe.explorer.validation;

import bg.softuni.recipe.explorer.service.RatingService;
import bg.softuni.recipe.explorer.service.RecipeService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class RecipeIdValidator implements ConstraintValidator<RecipeIdValid, Long> {

    private final RecipeService recipeService;

    @Autowired
    public RecipeIdValidator(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    public boolean isValid(Long recipeId, ConstraintValidatorContext context) {
        return this.recipeService.isIdValid(recipeId);
    }
}
