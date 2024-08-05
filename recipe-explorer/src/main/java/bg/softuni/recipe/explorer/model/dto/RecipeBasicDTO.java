package bg.softuni.recipe.explorer.model.dto;

public class RecipeBasicDTO {

    private Long id;

    private  String name;

    public RecipeBasicDTO() {}

    public Long getId() {
        return id;
    }

    public RecipeBasicDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public RecipeBasicDTO setName(String name) {
        this.name = name;
        return this;
    }
}
