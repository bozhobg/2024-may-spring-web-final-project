package bg.softuni.recipe.explorer.comments.service;

import bg.softuni.recipe.explorer.comments.exception.CommentNotFound;
import bg.softuni.recipe.explorer.comments.exception.CommentsNotFoundForRecipe;
import bg.softuni.recipe.explorer.comments.model.dto.CommentAddDTO;
import bg.softuni.recipe.explorer.comments.model.dto.CommentViewDTO;
import bg.softuni.recipe.explorer.comments.model.entity.Comment;
import bg.softuni.recipe.explorer.comments.repository.CommentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static bg.softuni.recipe.explorer.comments.constants.ErrorMessages.COMMENT_NOT_FOUND_FORMAT;
import static bg.softuni.recipe.explorer.comments.constants.ErrorMessages.RECIPE_COMMENTS_NOT_FOUND_FORMAT;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(
            CommentRepository commentRepository,
            ModelMapper modelMapper
    ) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public CommentViewDTO get(Long id) {

        return mapToViewDTO(this.commentRepository.findById(id)
                .orElseThrow(() ->
                        new CommentNotFound(String.format(COMMENT_NOT_FOUND_FORMAT, id), id)));
    }

    @Override
    public List<CommentViewDTO> getAllForRecipe(Long recipeId) {
//        TODO: how to verify existence of recipe? On client side?

        List<CommentViewDTO> dtos = this.commentRepository.findAllByRecipeId(recipeId)
                .stream()
                .map(c -> modelMapper.map(c, CommentViewDTO.class))
                .toList();

        if (dtos.isEmpty()) {
            throw new CommentsNotFoundForRecipe(
                    String.format(RECIPE_COMMENTS_NOT_FOUND_FORMAT, recipeId),
                    recipeId
            );
        }

        return dtos;
    }


    @Override
    public CommentViewDTO add(CommentAddDTO addDTO, Long recipeId) {
//        TODO: errs and handling?

        Comment newComment = mapToEntity(addDTO, recipeId);

        this.commentRepository.save(newComment);

        return mapToViewDTO(newComment);
    }

    @Override
    public CommentViewDTO approve(Long id) {

        Comment entity = this.commentRepository.findById(id)
                .orElseThrow(() ->
                        new CommentNotFound(String.format(COMMENT_NOT_FOUND_FORMAT, id), id));

        return mapToViewDTO(
                this.commentRepository.save(
                        entity.setApproved(true)
                                .setModifiedOn(Instant.now()))
        );
    }

    @Override
    public void delete(Long id) {
//        TODO: should it throw for not existing?

        this.commentRepository.deleteById(id);
    }

    private CommentViewDTO mapToViewDTO(Comment entity) {

        return modelMapper.map(entity, CommentViewDTO.class);
    }

    private Comment mapToEntity(CommentAddDTO addDTO, Long recipeId) {

        return modelMapper.map(addDTO, Comment.class)
                .setId(null)
                .setRecipeId(recipeId)
                .setCreateOn(Instant.now())
                .setModifiedOn(Instant.now())
                .setRecipeId(recipeId);
    }
}
