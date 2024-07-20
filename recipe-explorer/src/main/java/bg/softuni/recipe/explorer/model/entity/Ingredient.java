package bg.softuni.recipe.explorer.model.entity;

import bg.softuni.recipe.explorer.model.enums.IngredientType;
import bg.softuni.recipe.explorer.model.enums.UnitEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private UnitEnum unit;

    @Enumerated(EnumType.STRING)
    private IngredientType type;

    public Ingredient(){}

    public Long getId() {
        return id;
    }

    public Ingredient setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Ingredient setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Ingredient setDescription(String description) {
        this.description = description;
        return this;
    }

    public UnitEnum getUnit() {
        return unit;
    }

    public Ingredient setUnit(UnitEnum unit) {
        this.unit = unit;
        return this;
    }

    public IngredientType getType() {
        return type;
    }

    public Ingredient setType(IngredientType type) {
        this.type = type;
        return this;
    }
}
