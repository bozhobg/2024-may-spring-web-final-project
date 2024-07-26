package bg.softuni.recipe.explorer.model.dto;

public class IngredientBasicDTO {

    private Long id;

    private String name;

    public IngredientBasicDTO() {}

    public Long getId() {
        return id;
    }

    public IngredientBasicDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public IngredientBasicDTO setName(String name) {
        this.name = name;
        return this;
    }
}
