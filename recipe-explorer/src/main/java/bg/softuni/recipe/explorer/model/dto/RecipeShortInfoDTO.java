package bg.softuni.recipe.explorer.model.dto;

import bg.softuni.recipe.explorer.model.entity.Diet;
import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.enums.DietaryType;
import bg.softuni.recipe.explorer.model.enums.MealType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RecipeShortInfoDTO {

    private Long id;

    private String name;

    private MealType mealType;

    private List<String> ingredientNames;

    private List<DietaryType> dietTypes;

//    private BigDecimal averageRating;

    public RecipeShortInfoDTO() {
        this.ingredientNames = new ArrayList<>();
        this.dietTypes = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public RecipeShortInfoDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public RecipeShortInfoDTO setName(String name) {
        this.name = name;
        return this;
    }

    public MealType getMealType() {
        return mealType;
    }

    public RecipeShortInfoDTO setMealType(MealType mealType) {
        this.mealType = mealType;
        return this;
    }

    public List<String> getIngredientNames() {
        return ingredientNames;
    }

    public RecipeShortInfoDTO setIngredientNames(List<String> ingredientNames) {
        this.ingredientNames = ingredientNames;
        return this;
    }

    public List<DietaryType> getDietTypes() {
        return dietTypes;
    }

    public RecipeShortInfoDTO setDietTypes(List<DietaryType> dietTypes) {
        this.dietTypes = dietTypes;
        return this;
    }
}
