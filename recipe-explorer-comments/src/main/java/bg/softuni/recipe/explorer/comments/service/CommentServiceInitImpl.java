package bg.softuni.recipe.explorer.comments.service;

import bg.softuni.recipe.explorer.comments.model.entity.Comment;
import bg.softuni.recipe.explorer.comments.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CommentServiceInitImpl {

    private final static Long USER_ID_BOUND = 9L;
    private final static Long RECIPE_ID_BOUND = 10L;
    private final static Long COMMENT_COUNT_BOUND = 4L;

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceInitImpl(
            CommentRepository commentRepository
    ) {
        this.commentRepository = commentRepository;
    }

    public void init() {

        if (this.commentRepository.count() > 0) return;

        Random random = new Random();
        List<Comment> newComments = new ArrayList<>();

        for (long recipeId = 1; recipeId <= RECIPE_ID_BOUND; recipeId++) {
            long commentsCount = random.nextLong(COMMENT_COUNT_BOUND);

            for (int c = 0; c < commentsCount; c++) {
                long userId = random.nextLong(USER_ID_BOUND) + 1;
                Instant now = Instant.now();

                newComments.add(
                        new Comment()
                                .setMessage(String.format("Comment %d, for recipe %d, by user %d", c + 1, recipeId, userId))
                                .setAuthorId(userId)
                                .setRecipeId(recipeId)
                                .setCreateOn(now)
                                .setModifiedOn(now)
                                .setApproved(random.nextBoolean())
                );
            }
        }

        this.commentRepository.saveAll(newComments);
    }
}
