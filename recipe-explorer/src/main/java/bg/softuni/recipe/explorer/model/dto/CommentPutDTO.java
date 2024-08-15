package bg.softuni.recipe.explorer.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentPutDTO {

    private Long id;

    @NotBlank(message = "{comment.put.message.not.blank}")
    @Size(min = 10, message = "{comment.put.message.length}")
    private String message;

    public CommentPutDTO() {}

    public Long getId() {
        return id;
    }

    public CommentPutDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommentPutDTO setMessage(String message) {
        this.message = message;
        return this;
    }
}
