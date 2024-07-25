package bg.softuni.recipe.explorer.validation;

import bg.softuni.recipe.explorer.constants.ErrorMessages;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileNotEmptyValidator.class)
public @interface FileNotEmpty {

    String message() default ErrorMessages.FILE_EMPTY;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}