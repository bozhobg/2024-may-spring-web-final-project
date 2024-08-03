package bg.softuni.recipe.explorer.comments.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

public class CommentAddDTO {

    private String message;

    private Long authorId;

    public CommentAddDTO() {}

    public String getMessage() {
        return message;
    }

    public CommentAddDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public CommentAddDTO setAuthorId(Long authorId) {
        this.authorId = authorId;
        return this;
    }
}
