package bg.softuni.recipe.explorer.comments.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

public class CommentViewDTO {

    private Long id;

    private String message;

    private Long authorId;

    private Long recipeId;

    private Instant createOn;

    private Instant modifiedOn;

    private boolean isApproved;

    public CommentViewDTO() {}

    public Long getId() {
        return id;
    }

    public CommentViewDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommentViewDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public CommentViewDTO setAuthorId(Long authorId) {
        this.authorId = authorId;
        return this;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public CommentViewDTO setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
        return this;
    }

    public Instant getCreateOn() {
        return createOn;
    }

    public CommentViewDTO setCreateOn(Instant createOn) {
        this.createOn = createOn;
        return this;
    }

    public Instant getModifiedOn() {
        return modifiedOn;
    }

    public CommentViewDTO setModifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public CommentViewDTO setApproved(boolean approved) {
        isApproved = approved;
        return this;
    }
}
