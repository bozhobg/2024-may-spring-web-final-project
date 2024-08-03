package bg.softuni.recipe.explorer.comments.exception;

public class CommentNotFound extends RuntimeException {

    private Long id;

    public CommentNotFound(String message, Long id) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public CommentNotFound setId(Long id) {
        this.id = id;
        return this;
    }
}
