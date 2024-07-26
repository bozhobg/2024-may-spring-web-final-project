package bg.softuni.recipe.explorer.model.dto;

import bg.softuni.recipe.explorer.model.entity.Diet;
import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.enums.DietaryType;
import bg.softuni.recipe.explorer.model.enums.MealType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsDTO {

    private Long id;

    private String name;

    private String instructions;

    private MealType mealType;

    private Instant createdOn;

    private Instant modifiedOn;

    private String authorUsername;

    private List<String> ingredientNames;

    private List<DietaryType> dietaryTypes;

    private BigDecimal averageRating;

    public RecipeDetailsDTO() {
        this.ingredientNames = new ArrayList<>();
        this.dietaryTypes = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public RecipeDetailsDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public RecipeDetailsDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getInstructions() {
        return instructions;
    }

    public RecipeDetailsDTO setInstructions(String instructions) {
        this.instructions = instructions;
        return this;
    }

    public MealType getMealType() {
        return mealType;
    }

    public RecipeDetailsDTO setMealType(MealType mealType) {
        this.mealType = mealType;
        return this;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public RecipeDetailsDTO setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public Instant getModifiedOn() {
        return modifiedOn;
    }

    public RecipeDetailsDTO setModifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public RecipeDetailsDTO setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
        return this;
    }

    public List<String> getIngredientNames() {
        return ingredientNames;
    }

    public RecipeDetailsDTO setIngredientNames(List<String> ingredientNames) {
        this.ingredientNames = ingredientNames;
        return this;
    }

    public List<DietaryType> getDietaryTypes() {
        return dietaryTypes;
    }

    public RecipeDetailsDTO setDietaryTypes(List<DietaryType> dietaryTypes) {
        this.dietaryTypes = dietaryTypes;
        return this;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public RecipeDetailsDTO setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
        return this;
    }
}
