package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.exceptions.ObjectNotFoundException;
import bg.softuni.recipe.explorer.model.dto.CommentEditDTO;
import bg.softuni.recipe.explorer.model.dto.CommentRestDTO;
import bg.softuni.recipe.explorer.model.dto.CommentRestPostDTO;
import bg.softuni.recipe.explorer.model.dto.CommentViewDTO;
import bg.softuni.recipe.explorer.service.CommentService;
import bg.softuni.recipe.explorer.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
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

    @Override
    public List<CommentViewDTO> getCommentsForRecipe(Long recipeId) {

//        TODO: handle error return from comments service?

        List<CommentRestDTO> body = restClient.get()
                .uri("/recipe/{id}", recipeId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (body == null) return List.of();

//        TODO: what if user or recipe doesn't exist on client side?
//        TODO: scheduled/on event clean up of comments for users and recipes data on comments service

        return body.stream()
                .map(this::mapToViewDTO)
                .toList();
    }

    @Override
    public void approve(Long id) {
        restClient.patch()
                .uri("/{id}/approve", id)
                .retrieve();
    }

    @Override
    public void delete(Long id) {

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
    public CommentRestDTO get(Long id) {

        return restClient.get()
                .uri("/{id}", id)
                .retrieve()
//                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
//                    JsonNode jsonNode = objectMapper.readTree(response.getBody());
//                    String message = jsonNode.get("message").asText();
//                    throw new ObjectNotFoundException(message);
//                }))
                .body(CommentRestDTO.class);
    }

    @Override
    public void edit(Long id, String message, Long authorId) {

        restClient.put()
                .uri("/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CommentEditDTO()
                        .setAuthorId(authorId)
                        .setMessage(message)
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
}
