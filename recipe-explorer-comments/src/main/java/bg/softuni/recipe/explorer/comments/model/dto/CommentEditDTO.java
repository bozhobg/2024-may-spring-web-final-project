package bg.softuni.recipe.explorer.comments.model.dto;

public class CommentEditDTO {

    private Long authorId;
    private String message;

    public CommentEditDTO() {}

    public Long getAuthorId() {
        return authorId;
    }

    public CommentEditDTO setAuthorId(Long authorId) {
        this.authorId = authorId;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommentEditDTO setMessage(String message) {
        this.message = message;
        return this;
    }
}
