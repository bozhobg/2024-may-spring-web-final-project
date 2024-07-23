package bg.softuni.recipe.explorer.model.dto;

public class DietBasicDTO {

    private Long id;
    private String type;

    public DietBasicDTO() {}

    public Long getId() {
        return id;
    }

    public DietBasicDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public DietBasicDTO setType(String type) {
        this.type = type;
        return this;
    }
}
