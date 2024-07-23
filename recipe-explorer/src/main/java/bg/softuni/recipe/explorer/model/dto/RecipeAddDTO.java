package bg.softuni.recipe.explorer.model.dto;

import bg.softuni.recipe.explorer.model.entity.Diet;
import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.enums.MealType;
import bg.softuni.recipe.explorer.validation.RecipeNameValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class RecipeAddDTO {

    @NotBlank
    @Size(min = 5, max = 30)
    @RecipeNameValid
    private String name;

    @NotBlank
    @Size(min = 50)
    private String instructions;

    @NotNull
    private MealType mealType;

    @NotEmpty
    private List<Ingredient> ingredients;

    private List<Long> dietIds;

    public RecipeAddDTO() {
        this.ingredients = new ArrayList<>();
        this.dietIds = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public RecipeAddDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getInstructions() {
        return instructions;
    }

    public RecipeAddDTO setInstructions(String instructions) {
        this.instructions = instructions;
        return this;
    }

    public MealType getMealType() {
        return mealType;
    }

    public RecipeAddDTO setMealType(MealType mealType) {
        this.mealType = mealType;
        return this;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public RecipeAddDTO setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public List<Long> getDiets() {
        return dietIds;
    }

    public RecipeAddDTO setDiets(List<Long> dietIds) {
        this.dietIds = dietIds;
        return this;
    }
}
