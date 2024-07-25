package bg.softuni.recipe.explorer.model.dto;

import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.model.enums.IngredientType;
import bg.softuni.recipe.explorer.model.enums.UnitEnum;
import jakarta.persistence.*;

public class IngredientShortInfoDTO {

    private Long id;

    private String name;

    private String description;

    private IngredientType type;

    public IngredientShortInfoDTO() {}

    public Long getId() {
        return id;
    }

    public IngredientShortInfoDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public IngredientShortInfoDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IngredientShortInfoDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public IngredientType getType() {
        return type;
    }

    public IngredientShortInfoDTO setType(IngredientType type) {
        this.type = type;
        return this;
    }
}
