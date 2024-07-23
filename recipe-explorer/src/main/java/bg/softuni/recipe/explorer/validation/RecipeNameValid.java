package bg.softuni.recipe.explorer.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RecipeNameValidator.class)
public @interface RecipeNameValid {

    String message() default "Invalid recipe name!";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
