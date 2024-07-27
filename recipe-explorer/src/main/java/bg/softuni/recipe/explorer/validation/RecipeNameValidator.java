package bg.softuni.recipe.explorer.validation;

import bg.softuni.recipe.explorer.model.dto.RecipeAddDTO;
import bg.softuni.recipe.explorer.service.RecipeService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class RecipeNameValidator implements ConstraintValidator<RecipeNameValid, String> {

    private final RecipeService recipeService;

    @Autowired
    public RecipeNameValidator(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return recipeService.isNameUnique(s);
    }
}
