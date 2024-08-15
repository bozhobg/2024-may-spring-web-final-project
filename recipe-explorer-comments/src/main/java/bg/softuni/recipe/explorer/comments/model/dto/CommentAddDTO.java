package bg.softuni.recipe.explorer.comments.model.dto;

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
