package bg.softuni.recipe.explorer.model.dto;

import bg.softuni.recipe.explorer.model.enums.IngredientType;
import bg.softuni.recipe.explorer.model.enums.UnitEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class IngredientAddDTO {

    @NotBlank
    @Size(min = 3, max = 20)
    private String name;

    @NotBlank
    @Size(min = 30)
    private String description;

    @NotNull
    private UnitEnum unit;

    @NotNull
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
