package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.constants.ExceptionMessages;
import bg.softuni.recipe.explorer.exceptions.ObjectNotFoundException;
import bg.softuni.recipe.explorer.model.dto.RecipeAddDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeDetailsDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeEditDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeShortInfoDTO;
import bg.softuni.recipe.explorer.model.entity.*;
import bg.softuni.recipe.explorer.repository.RecipeRepository;
import bg.softuni.recipe.explorer.service.DietService;
import bg.softuni.recipe.explorer.service.IngredientService;
import bg.softuni.recipe.explorer.service.RecipeService;
import bg.softuni.recipe.explorer.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;
    private final DietService dietService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public RecipeServiceImpl(
            RecipeRepository recipeRepository,
            IngredientService ingredientService,
            DietService dietService,
            UserService userService,
            ModelMapper modelMapper
    ) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
        this.dietService = dietService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean isIdValid(Long id) {
        return this.recipeRepository.existsById(id);
    }

    @Override
    public boolean isNameUnique(String name) {
        return !this.recipeRepository.existsByName(name);
    }

    @Override
    public Recipe getById(Long id) {
        return this.recipeRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Recipe not found"));
    }

    @Override
    public List<RecipeShortInfoDTO> getAllShort() {

        List<RecipeShortInfoDTO> allShort = this.recipeRepository.findAll()
                .stream()
                .map(this::mapToShort)
                .toList();

        return allShort;
    }

    @Override
    public RecipeDetailsDTO getDetailsById(Long id) {

        RecipeDetailsDTO dto = mapToDetails(this.recipeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(ExceptionMessages.RECIPE_NOT_FOUND)));

        return dto;
    }

    @Override
    public Long add(RecipeAddDTO dto, Long userId) {
        Recipe newRecipe = mapAddToEntity(dto, userId);

        return this.recipeRepository
                .save(newRecipe)
                .getId();
    }

    @Override
    public RecipeEditDTO getEditDTO(Long id) {
        Recipe recipe = this.recipeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(ExceptionMessages.RECIPE_NOT_FOUND));

        RecipeEditDTO map = modelMapper.map(recipe, RecipeEditDTO.class);

        return map;
    }

    @Override
    public void put(Long recipeId, RecipeEditDTO dto) {
        Recipe updated = mapEditToEntity(dto, recipeId);

        this.recipeRepository.save(updated);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
//            TODO: invalid Long value
            throw new ObjectNotFoundException("Recipe id invalid!");
        }

        // ratings clean up
        Recipe recipe = this.recipeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(ExceptionMessages.RECIPE_NOT_FOUND));

        this.recipeRepository.delete(recipe);
    }

    @Override
    public void updateAvgRating(Long recipeId, BigDecimal averageRating) {

        Recipe recipe = this.recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ObjectNotFoundException(ExceptionMessages.RECIPE_NOT_FOUND));
        recipe.setAverageRating(averageRating);
        this.recipeRepository.save(recipe);
    }


    private Recipe mapAddToEntity(RecipeAddDTO dto, Long userId) {
        Recipe map = modelMapper.map(dto, Recipe.class);

        Set<Diet> diets = dietService.getAllByIds(dto.getDietIds());
        Set<Ingredient> ingredients = ingredientService.getAllByIds(dto.getIngredientIds());
        User author = this.userService.getUserById(userId);

        map.setCreatedOn(Instant.now())
                .setModifiedOn(Instant.now())
                .setAuthor(author)
                .setIngredients(ingredients)
                .setDiets(diets);

        return map;
    }


    private Recipe mapEditToEntity(RecipeEditDTO dto, Long recipeId) {

        Recipe recipe = this.recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ObjectNotFoundException(ExceptionMessages.RECIPE_NOT_FOUND));

        Set<Diet> diets = dietService.getAllByIds(dto.getDietIds());
        Set<Ingredient> ingredients = ingredientService.getAllByIds(dto.getIngredientIds());

//        setting only editable fields

        return recipe.setName(dto.getName())
                .setModifiedOn(Instant.now())
                .setInstructions(dto.getInstructions())
                .setIngredients(ingredients)
                .setDiets(diets);
    }

    private RecipeShortInfoDTO mapToShort(Recipe entity) {
        RecipeShortInfoDTO map = modelMapper.map(entity, RecipeShortInfoDTO.class);
        return map;
    }

    private RecipeDetailsDTO mapToDetails(Recipe entity) {
        RecipeDetailsDTO map = modelMapper.map(entity, RecipeDetailsDTO.class);
        return map;
    }
}
