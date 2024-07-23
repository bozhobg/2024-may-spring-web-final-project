package bg.softuni.recipe.explorer.validation;

import bg.softuni.recipe.explorer.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailValidator implements ConstraintValidator<EmailValid, String> {

    private final UserService userService;

    @Autowired
    public EmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return this.userService.isEmailUnique(value);
    }
}
