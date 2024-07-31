package bg.softuni.recipe.explorer.validation;

import bg.softuni.recipe.explorer.service.IngredientService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class IngredientNameValidator implements ConstraintValidator<IngredientNameValid, String> {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientNameValidator(
            IngredientService ingredientService
    ) {
        this.ingredientService = ingredientService;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return this.ingredientService.isNameUnique(s);
    }
}
