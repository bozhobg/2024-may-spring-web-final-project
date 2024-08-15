package bg.softuni.recipe.explorer.model.dto;

import bg.softuni.recipe.explorer.model.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;

public class RoleDTO {

    @NotNull(message = "Role must not be null")
    private RoleEnum name;

    private String username;

    public RoleDTO() {}

    public RoleEnum getName() {
        return name;
    }

    public RoleDTO setName(RoleEnum name) {
        this.name = name;
        return this;
    }
}
