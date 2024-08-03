package bg.softuni.recipe.explorer.comments.exception;

public class CommentsNotFoundForRecipe extends RuntimeException{

    private Long id;

    public CommentsNotFoundForRecipe(String message, Long id) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public CommentsNotFoundForRecipe setId(Long id) {
        this.id = id;
        return this;
    }
}
