package bg.softuni.recipe.explorer.comments.model.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "created_on", nullable = false)
    private Instant createOn;

    @Column(name = "modified_on")
    private Instant modifiedOn;

    @Column(name = "approved", nullable = false)
    private boolean isApproved;

    public Comment() {}

    public Long getId() {
        return id;
    }

    public Comment setId(Long id) {
        this.id = id;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Comment setMessage(String message) {
        this.message = message;
        return this;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public Comment setAuthorId(Long authorId) {
        this.authorId = authorId;
        return this;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public Comment setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
        return this;
    }

    public Instant getCreateOn() {
        return createOn;
    }

    public Comment setCreateOn(Instant createOn) {
        this.createOn = createOn;
        return this;
    }

    public Instant getModifiedOn() {
        return modifiedOn;
    }

    public Comment setModifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public Comment setApproved(boolean approved) {
        isApproved = approved;
        return this;
    }
}
