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

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private UnitEnum unit;

    @Enumerated(EnumType.STRING)
    @Column(name = "ingredient_type")
    private IngredientType ingredientType;

//    TODO: added by, modified by, created on, modified on

    @ManyToOne(optional = false)
    @JoinColumn(name = "added_by_id")
    private User addedBy;

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

    public IngredientType getIngredientType() {
        return ingredientType;
    }

    public Ingredient setIngredientType(IngredientType type) {
        this.ingredientType = type;
        return this;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public Ingredient setAddedBy(User addedBy) {
        this.addedBy = addedBy;
        return this;
    }
}
