package bg.softuni.recipe.explorer.model.entity;

import bg.softuni.recipe.explorer.model.enums.DietaryType;
import jakarta.persistence.*;

@Entity
@Table(name = "diets")
public class Diet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "dietary_type")
    private DietaryType dietaryType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    public Diet() {}

    public Long getId() {
        return id;
    }

    public Diet setId(Long id) {
        this.id = id;
        return this;
    }

    public DietaryType getDietaryType() {
        return dietaryType;
    }

    public Diet setDietaryType(DietaryType dietaryType) {
        this.dietaryType = dietaryType;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Diet setDescription(String description) {
        this.description = description;
        return this;
    }
}
