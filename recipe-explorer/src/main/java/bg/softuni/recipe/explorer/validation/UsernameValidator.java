package bg.softuni.recipe.explorer.validation;

import bg.softuni.recipe.explorer.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UsernameValidator implements ConstraintValidator<UsernameValid, String> {

    private final UserService userService;

    @Autowired
    public UsernameValidator(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return this.userService.isUsernameUnique(value);
    }
}
