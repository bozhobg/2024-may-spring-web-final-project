package bg.softuni.recipe.explorer.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RecipeIdValidator.class)
public @interface RecipeIdValid {

    String message() default "Invalid recipe id!";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
