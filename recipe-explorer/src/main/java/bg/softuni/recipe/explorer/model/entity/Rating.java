package bg.softuni.recipe.explorer.model.entity;

import bg.softuni.recipe.explorer.model.enums.RatingEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RatingEnum rating;

    @ManyToOne(optional = false)
    private Recipe recipe;

    @ManyToOne(optional = false)
    private User user;

     public Rating() {}

    public Long getId() {
        return id;
    }

    public Rating setId(Long id) {
        this.id = id;
        return this;
    }

    public RatingEnum getRating() {
        return rating;
    }

    public Rating setRating(RatingEnum rating) {
        this.rating = rating;
        return this;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Rating setRecipe(Recipe recipe) {
        this.recipe = recipe;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Rating setUser(User user) {
        this.user = user;
        return this;
    }
}
