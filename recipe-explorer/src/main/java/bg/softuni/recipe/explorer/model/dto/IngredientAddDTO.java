package bg.softuni.recipe.explorer.model.dto;

import bg.softuni.recipe.explorer.model.enums.IngredientType;
import bg.softuni.recipe.explorer.model.enums.UnitEnum;
import bg.softuni.recipe.explorer.validation.IngredientNameValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class IngredientAddDTO {

    @NotBlank(message = "{ingredient.add.name.not.blank}")
    @Size(min = 3, max = 20, message = "{ingredient.add.name.size}")
    @IngredientNameValid(message = "{ingredient.add.name.invalid}")
    private String name;

    @NotBlank(message = "{ingredient.add.description.not.blank}")
    @Size(min = 30, message = "{ingredient.add.description.size}")
    private String description;

    @NotNull(message = "{ingredient.add.unit.not.null}")
    private UnitEnum unit;

    @NotNull(message = "{ingredient.add.type.not.null}")
    private IngredientType type;

    public IngredientAddDTO() {}

    public String getName() {
        return name;
    }

    public IngredientAddDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IngredientAddDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public UnitEnum getUnit() {
        return unit;
    }

    public IngredientAddDTO setUnit(UnitEnum unit) {
        this.unit = unit;
        return this;
    }

    public IngredientType getType() {
        return type;
    }

    public IngredientAddDTO setType(IngredientType type) {
        this.type = type;
        return this;
    }
}
