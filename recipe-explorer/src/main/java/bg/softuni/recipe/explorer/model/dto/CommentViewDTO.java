package bg.softuni.recipe.explorer.model.dto;

import java.time.Instant;

public class CommentViewDTO {

    private Long id;
    private String username;
    private String message;
    private Instant modifiedOn;

    public CommentViewDTO() {}

    public Long getId() {
        return id;
    }

    public CommentViewDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public CommentViewDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommentViewDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    public Instant getModifiedOn() {
        return modifiedOn;
    }

    public CommentViewDTO setModifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }
}
