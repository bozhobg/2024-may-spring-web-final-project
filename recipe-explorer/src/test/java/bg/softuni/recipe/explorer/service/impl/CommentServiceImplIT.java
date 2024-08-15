package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.config.CommentsApiConfig;
import bg.softuni.recipe.explorer.model.dto.CommentRestDTO;
import bg.softuni.recipe.explorer.model.dto.CommentViewDTO;
import bg.softuni.recipe.explorer.service.CommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableWireMock(
        @ConfigureWireMock(name = "comment-service")
)
public class CommentServiceImplIT {

    private static class TestCommentRestDTOs {
        private static final CommentRestDTO test1 =
                new CommentRestDTO()
                        .setId(1L)
                        .setMessage("Comment 1, for recipe 1, by user 1")
                        .setRecipeId(1L)
                        .setAuthorId(1L)
                        .setApproved(false)
                        .setCreateOn(Instant.now())
                        .setModifiedOn(Instant.now());

        private static final CommentRestDTO test2 =
                new CommentRestDTO()
                        .setId(2L)
                        .setMessage("Comment 2, for recipe 1, by user 2")
                        .setRecipeId(1L)
                        .setAuthorId(2L)
                        .setApproved(true)
                        .setCreateOn(Instant.now())
                        .setModifiedOn(Instant.now());
    }

    @InjectWireMock("comment-service")
    private WireMockServer wireMock;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentsApiConfig commentsApiConfig;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        commentsApiConfig.setBaseUrl(wireMock.baseUrl() + "/test-comments");
    }

//    TODO: rest client base url not updated? send requests to standard config url

    @Test
    void testGetCommentsForRecipeThrowsForInvalidRecipe() {
        wireMock.stubFor(get("/test-comments/recipe/" + 99).willReturn(notFound()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                        {
                        	"id": 99,
                        	"code": "NOT FOUND",
                        	"message": "Comments not found for recipe with id: 99 !"
                        }
                        """)));

        Assertions.assertThrows(HttpClientErrorException.NotFound.class, () -> commentService.getCommentsForRecipe(99L));
    }

    @Test
    void testGetCommentsForRecipeWillReturnTwoCommentRestDTOs() throws JsonProcessingException {
        wireMock.stubFor(get("test-comments/recipe" + 1).willReturn(notFound()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(objectMapper.writeValueAsString(
                        List.of(TestCommentRestDTOs.test1, TestCommentRestDTOs.test2)))
        ));

        List<CommentViewDTO> actualDTOs = this.commentService.getCommentsForRecipe(1L);
        assertEquals(2, actualDTOs.size());

        for (CommentViewDTO dto : actualDTOs) {

        }
    }

    @Test
    void testGetCommentThrowsForInvalidRecipeId() {
        wireMock.stubFor(get("/test-comments/" + 99).willReturn(notFound()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                        {
                        	"id": 99,
                        	"code": "NOT FOUND",
                        	"message": "Comment not found with id:  99 !"
                        }
                        """)));

        Assertions.assertThrows(HttpClientErrorException.NotFound.class, () -> commentService.get(99L, appUserDetails));
    }


}
