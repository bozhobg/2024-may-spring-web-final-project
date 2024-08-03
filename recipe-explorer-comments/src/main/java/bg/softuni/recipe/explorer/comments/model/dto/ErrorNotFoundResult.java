package bg.softuni.recipe.explorer.comments.model.dto;

public record ErrorNotFoundResult(
        Long id,
        String code,
        String message
) {}
