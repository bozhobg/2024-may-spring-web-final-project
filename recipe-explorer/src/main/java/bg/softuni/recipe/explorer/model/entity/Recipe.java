package bg.softuni.recipe.explorer.model.entity;

import bg.softuni.recipe.explorer.model.enums.MealType;
import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recipes")
public class Recipe {

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

    @ManyToOne(optional = false)
    private User author;

//    TODO: how to make relation ingredient-recipe-amount(units) for amount of servings
//    TODO: How to use @Embeddable and @Embed to add extra column for quantities
    @ManyToMany
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredient> ingredients;

    @ManyToMany
    @JoinTable(
            name = "recipe_diet",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "diet_id")
    )
    private Set<Diet> diets;

    @OneToMany(mappedBy = "recipe")
    private Set<Rating> ratings;

//  TODO: private Set<Comments> comments;

    private BigDecimal averageRating;

    public Recipe() {
        this.ingredients = new HashSet<>();
        this.diets = new HashSet<>();
        this.ratings = new HashSet<>();
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

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public Recipe setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public Set<Diet> getDiets() {
        return diets;
    }

    public Recipe setDiets(Set<Diet> diets) {
        this.diets = diets;
        return this;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public Recipe setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
        return this;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public Recipe updateAverageRating() {

        int sum = this.ratings.stream()
                .mapToInt(r -> r.getRating().getAsInt())
                .sum();

        int size = this.ratings.size();

        if (size == 0) {
            this.averageRating = BigDecimal.ZERO;
        } else {
            this.averageRating = BigDecimal.valueOf(sum)
                    .divide(BigDecimal.valueOf(size), 2, RoundingMode.HALF_UP);
        }

        return this;
    }
}
