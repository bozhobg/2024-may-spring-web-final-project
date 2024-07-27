package bg.softuni.recipe.explorer.validation;

import bg.softuni.recipe.explorer.service.IngredientService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;


public class IngredientIdsValidor implements ConstraintValidator<IngredientIdsValid, List<Long>> {

    private final IngredientService ingredientService;

    public IngredientIdsValidor(
            IngredientService ingredientService
    ) {
        this.ingredientService = ingredientService;
    }

    @Override
    public boolean isValid(List<Long> listIds, ConstraintValidatorContext context) {
        return ingredientService.areIdsValid(listIds);
    }
}
