package bg.softuni.recipe.explorer.validation;

import bg.softuni.recipe.explorer.service.DietService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DietIdsValidator implements ConstraintValidator<DietIdsValid, List<Long>> {

    private final DietService dietService;

    @Autowired
    public DietIdsValidator(DietService dietService) {
        this.dietService = dietService;
    }


    @Override
    public boolean isValid(List<Long> listIds, ConstraintValidatorContext context) {
        return this.dietService.areIdsValid(listIds);
    }
}
