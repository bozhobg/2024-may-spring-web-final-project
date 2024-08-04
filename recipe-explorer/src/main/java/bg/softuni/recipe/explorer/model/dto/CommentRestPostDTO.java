package bg.softuni.recipe.explorer.model.dto;

public class CommentRestPostDTO {

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
