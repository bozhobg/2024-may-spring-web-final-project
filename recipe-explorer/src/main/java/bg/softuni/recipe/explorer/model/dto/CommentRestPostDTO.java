package bg.softuni.recipe.explorer.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentRestPostDTO {

    @NotBlank
    @Size(min = 10)
    private String message;

    private Long authorId;

    public CommentRestPostDTO() {}

    public String getMessage() {
        return message;
    }

    public CommentRestPostDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public CommentRestPostDTO setAuthorId(Long authorId) {
        this.authorId = authorId;
        return this;
    }
}
