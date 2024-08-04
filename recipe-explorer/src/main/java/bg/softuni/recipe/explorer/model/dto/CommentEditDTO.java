package bg.softuni.recipe.explorer.model.dto;

public class CommentEditDTO {

    private String message;
    private Long authorId;

    public String getMessage() {
        return message;
    }

    public CommentEditDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public CommentEditDTO setAuthorId(Long authorId) {
        this.authorId = authorId;
        return this;
    }
}
