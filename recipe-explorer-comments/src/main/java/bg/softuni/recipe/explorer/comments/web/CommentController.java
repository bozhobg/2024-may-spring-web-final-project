package bg.softuni.recipe.explorer.comments.web;

import bg.softuni.recipe.explorer.comments.exception.CommentNotFound;
import bg.softuni.recipe.explorer.comments.exception.CommentsNotFoundForRecipe;
import bg.softuni.recipe.explorer.comments.model.dto.CommentAddDTO;
import bg.softuni.recipe.explorer.comments.model.dto.CommentEditDTO;
import bg.softuni.recipe.explorer.comments.model.dto.CommentViewDTO;
import bg.softuni.recipe.explorer.comments.model.dto.ErrorNotFoundResult;
import bg.softuni.recipe.explorer.comments.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(
            CommentService commentService
    ) {
        this.commentService = commentService;
    }

//    TODO: get, post, edit - put/patch, delete

    @GetMapping("/{id}")
    public ResponseEntity<CommentViewDTO> getComment(
            @PathVariable("id") Long id
    ) {
//        TODO: logic, custom err handling

        return ResponseEntity.ok(this.commentService.get(id));
    }

    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<CommentViewDTO>> getRecipeComments(
            @PathVariable("recipeId") Long recipeId
    ) {

        return ResponseEntity.ok(this.commentService.getAllForRecipe(recipeId));
    }

    @PostMapping("/recipe/{recipeId}")
    public ResponseEntity<CommentViewDTO> postRecipeComment(
            @RequestBody CommentAddDTO bindingModel,
            @PathVariable("recipeId") Long recipeId
    ) {
        CommentViewDTO commentView = this.commentService.add(bindingModel, recipeId);

        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/comments/{id}")
                        .buildAndExpand(commentView.getId())
                        .toUri()
        ).body(commentView);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<CommentViewDTO> approveComment(
            @PathVariable("id") Long id
    ) {
        CommentViewDTO view = this.commentService.approve(id);

        return ResponseEntity.ok(view);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentViewDTO> editComment(
            @PathVariable("id") Long id,
            @RequestBody CommentEditDTO bindingModel
    ) {
        CommentViewDTO view = this.commentService.edit(bindingModel, id);

        return ResponseEntity.ok(view);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommentViewDTO> deleteComment(
            @PathVariable("id") Long id
    ) {
        this.commentService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CommentsNotFoundForRecipe.class)
    @ResponseBody
    public ErrorNotFoundResult commentsNotFound(CommentsNotFoundForRecipe exc) {
        return new ErrorNotFoundResult(exc.getId(), "NOT FOUND", exc.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CommentNotFound.class)
    @ResponseBody
    public ErrorNotFoundResult commentNotFound(CommentNotFound exc) {
        return new ErrorNotFoundResult(exc.getId(), "NOT FOUND", exc.getMessage());
    }
}
