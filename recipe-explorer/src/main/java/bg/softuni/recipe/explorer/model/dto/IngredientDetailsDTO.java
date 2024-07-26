package bg.softuni.recipe.explorer.model.dto;

import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.model.enums.IngredientType;
import bg.softuni.recipe.explorer.model.enums.UnitEnum;
import jakarta.persistence.*;

public class IngredientDetailsDTO {


    private Long id;

    private String name;

    private String description;

    private UnitEnum unit;

    private IngredientType type;

//    TODO: added by, modified by, created on, modified on

    private String addedByUsername;

    public IngredientDetailsDTO() {}

    public Long getId() {
        return id;
    }

    public IngredientDetailsDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public IngredientDetailsDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IngredientDetailsDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public UnitEnum getUnit() {
        return unit;
    }

    public IngredientDetailsDTO setUnit(UnitEnum unit) {
        this.unit = unit;
        return this;
    }

    public IngredientType getType() {
        return type;
    }

    public IngredientDetailsDTO setType(IngredientType type) {
        this.type = type;
        return this;
    }

    public String getAddedByUsername() {
        return addedByUsername;
    }

    public IngredientDetailsDTO setAddedByUsername(String addedByUsername) {
        this.addedByUsername = addedByUsername;
        return this;
    }
}
