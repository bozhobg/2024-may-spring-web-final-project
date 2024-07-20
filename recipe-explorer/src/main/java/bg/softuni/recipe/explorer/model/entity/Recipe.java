package bg.softuni.recipe.explorer.model.entity;

import bg.softuni.recipe.explorer.model.enums.MealType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recipes")
public class Recipe {
//    TODO:
//    TODO: how to make relation ingredient-recipe-amount(units) for amount of servings

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String instructions;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal")
    private MealType mealType;

    @Column(name = "crated_on")
    private Instant createdOn;

    @Column(name = "modified_on")
    private Instant modifiedOn;

    @ManyToOne
    private User author;

    @ManyToMany
    @JoinTable(
            name = "recipe_diet",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "diet_id")
    )
    private Set<Diet> diets;

//    private Set<Comments> comments;
//
//    private Set<Rating> ratings;
//
//    private BigDecimal avgerageRating;

    public Recipe() {
        this.diets = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public Recipe setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Recipe setName(String name) {
        this.name = name;
        return this;
    }

    public String getInstructions() {
        return instructions;
    }

    public Recipe setInstructions(String instructions) {
        this.instructions = instructions;
        return this;
    }

    public MealType getMealType() {
        return mealType;
    }

    public Recipe setMealType(MealType mealType) {
        this.mealType = mealType;
        return this;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Recipe setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public Instant getModifiedOn() {
        return modifiedOn;
    }

    public Recipe setModifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public User getAuthor() {
        return author;
    }

    public Recipe setAuthor(User author) {
        this.author = author;
        return this;
    }

    public Set<Diet> getDiets() {
        return diets;
    }

    public Recipe setDiets(Set<Diet> diets) {
        this.diets = diets;
        return this;
    }
}
