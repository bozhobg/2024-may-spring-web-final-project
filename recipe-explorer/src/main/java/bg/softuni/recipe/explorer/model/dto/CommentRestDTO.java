package bg.softuni.recipe.explorer.model.dto;

import java.time.Instant;

public class CommentRestDTO {
    private Long id;

    private String message;

    private Long authorId;

    private Long recipeId;

    private Instant createOn;

    private Instant modifiedOn;

    private boolean isApproved;

    public CommentRestDTO() {}

    public Long getId() {
        return id;
    }

    public CommentRestDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommentRestDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public CommentRestDTO setAuthorId(Long authorId) {
        this.authorId = authorId;
        return this;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public CommentRestDTO setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
        return this;
    }

    public Instant getCreateOn() {
        return createOn;
    }

    public CommentRestDTO setCreateOn(Instant createOn) {
        this.createOn = createOn;
        return this;
    }

    public Instant getModifiedOn() {
        return modifiedOn;
    }

    public CommentRestDTO setModifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public CommentRestDTO setApproved(boolean approved) {
        isApproved = approved;
        return this;
    }
}
