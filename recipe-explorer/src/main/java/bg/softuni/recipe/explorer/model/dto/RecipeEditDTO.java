package bg.softuni.recipe.explorer.model.dto;

import bg.softuni.recipe.explorer.model.enums.MealType;
import bg.softuni.recipe.explorer.validation.DietIdsValid;
import bg.softuni.recipe.explorer.validation.IngredientIdsValid;
import bg.softuni.recipe.explorer.validation.RecipeNameValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class RecipeEditDTO {

    @NotBlank(message = "{recipe.add.name.not.blank}")
    @Size(min = 5, max = 30, message="{recipe.add.name.size}")
    private String name;

    @NotBlank(message="{recipe.add.instructions.not.blank}")
    @Size(min = 50, message = "{recipe.add.instructions.size}")
    private String instructions;

    @NotNull(message="{recipe.add.meal.type.not.null}")
    private MealType mealType;

//    TODO: must not be empty error message from where?
    @NotEmpty(message = "{recipe.add.ingredient.ids.not.empty}")
    @IngredientIdsValid(message = "{recipe.add.ingredient.ids.valid}")
    private List<Long> ingredientIds;

    @DietIdsValid(message = "{recipe.add.diet.ids.valid}")
    private List<Long> dietIds;

    public RecipeEditDTO() {
        this.ingredientIds = new ArrayList<>();
        this.dietIds = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public RecipeEditDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getInstructions() {
        return instructions;
    }

    public RecipeEditDTO setInstructions(String instructions) {
        this.instructions = instructions;
        return this;
    }

    public MealType getMealType() {
        return mealType;
    }

    public RecipeEditDTO setMealType(MealType mealType) {
        this.mealType = mealType;
        return this;
    }

    public List<Long> getIngredientIds() {
        return ingredientIds;
    }

    public RecipeEditDTO setIngredientIds(List<Long> ingredientIds) {
        this.ingredientIds = ingredientIds;
        return this;
    }

    public List<Long> getDietIds() {
        return dietIds;
    }

    public RecipeEditDTO setDietIds(List<Long> dietIds) {
        this.dietIds = dietIds;
        return this;
    }
}
