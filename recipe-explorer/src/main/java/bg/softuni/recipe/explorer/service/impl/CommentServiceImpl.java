package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.constants.ExceptionMessages;
import bg.softuni.recipe.explorer.exceptions.UnauthorizedOperation;
import bg.softuni.recipe.explorer.model.dto.*;
import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import bg.softuni.recipe.explorer.service.CommentService;
import bg.softuni.recipe.explorer.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;


@Service
public class CommentServiceImpl implements CommentService {

    private final RestClient restClient;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommentServiceImpl(
            RestClient restClient,
            UserService userService,
            ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

//  TODO: scheduled/on event clean up of comments for users and recipes data on comments service

    @Override
    public List<CommentViewDTO> getCommentsForRecipe(Long recipeId) {

//        TODO: handle error return from comments service?

        List<CommentRestDTO> body = restClient.get()
                .uri("/recipe/{id}", recipeId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (body == null) return List.of();

        return body.stream()
                .map(this::mapToViewDTO)
                .toList();
    }

    @Override
    public void approve(Long id) {
//        role requirement through spring security config

        restClient.patch()
                .uri("/{id}/approve", id)
                .retrieve();
    }

    @Override
    public void delete(Long id, AppUserDetails appUserDetails) {
        verifyAuthorModeratorOrAdmin(id, appUserDetails);

        restClient.delete()
                .uri("/{id}", id)
                .retrieve();
    }

    @Override
    public void post(String message, Long recipeId, Long authorId) {

        restClient.post()
                .uri("/recipe/{recipeId}", recipeId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CommentRestPostDTO()
                        .setAuthorId(authorId)
                        .setMessage(message))
                .retrieve();
    }

    @Override
    public CommentPutDTO get(Long id, AppUserDetails appUserDetails) {
        verifyAuthor(id, appUserDetails);

        CommentRestDTO commentData = restClient.get()
                .uri("/{id}", id)
                .retrieve()
//                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
//                    JsonNode jsonNode = objectMapper.readTree(response.getBody());
//                    String message = jsonNode.get("message").asText();
//                    throw new ObjectNotFoundException(message);
//                }))
                .body(CommentRestDTO.class);

        return new CommentPutDTO()
                .setId(commentData.getId())
                .setMessage(commentData.getMessage());
    }

    @Override
    public void edit(CommentPutDTO dto, AppUserDetails appUserDetails) {

        if (dto.getId() == null) throw new IllegalArgumentException("Comment id should not be null");
        verifyAuthor(dto.getId(), appUserDetails);

        restClient.put()
                .uri("/{id}", dto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CommentEditDTO()
                        .setAuthorId(appUserDetails.getId())
                        .setMessage(dto.getMessage())
                ).retrieve();
    }

    private CommentViewDTO mapToViewDTO(CommentRestDTO dto) {

        return new CommentViewDTO()
                .setApproved(dto.isApproved())
                .setUsername(
                        this.userService.getUserById(dto.getAuthorId()).getUsername()
                ).setModifiedOn(dto.getModifiedOn())
                .setMessage(dto.getMessage())
                .setId(dto.getId());
    }

    private void verifyModeratorOrAdmin(AppUserDetails appUserDetails) {

        if (!appUserDetails.isModerator() || !appUserDetails.isAdmin()) {
            throw new UnauthorizedOperation(ExceptionMessages.UNAUTHORIZED_REQUEST);
        }
    }

    private void verifyAuthor(Long commentId, AppUserDetails appUserDetails) {
        Long authorId = appUserDetails.getId();
        CommentRestDTO dto = restClient.get()
                .uri("/{id}", commentId)
                .retrieve()
                .body(CommentRestDTO.class);

        if (dto == null || !dto.getAuthorId().equals(authorId)) {
            throw new UnauthorizedOperation(ExceptionMessages.UNAUTHORIZED_REQUEST);
        }
    }

    private void verifyAuthorModeratorOrAdmin(Long commentId, AppUserDetails appUserDetails) {
        verifyModeratorOrAdmin(appUserDetails);
        verifyAuthor(commentId, appUserDetails);
    }
}
