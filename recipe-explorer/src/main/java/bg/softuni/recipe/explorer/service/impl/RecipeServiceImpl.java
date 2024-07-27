package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.exceptions.ObjectNotFoundException;
import bg.softuni.recipe.explorer.model.dto.RecipeAddDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeDetailsDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeShortInfoDTO;
import bg.softuni.recipe.explorer.model.entity.Diet;
import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.entity.Recipe;
import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.repository.RecipeRepository;
import bg.softuni.recipe.explorer.service.DietService;
import bg.softuni.recipe.explorer.service.IngredientService;
import bg.softuni.recipe.explorer.service.RecipeService;
import bg.softuni.recipe.explorer.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean isNameUnique(String name) {
        return !this.recipeRepository.existsByName(name);
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
                .orElseThrow(() -> new ObjectNotFoundException("Recipe not found!")));

        return dto;
    }

    @Override
    public Long add(RecipeAddDTO dto, Long userId) {
        Recipe newRecipe = mapToEntity(dto, userId);

        return this.recipeRepository
                .save(newRecipe)
                .getId();
    }

    private Recipe mapToEntity(RecipeAddDTO dto, Long userId) {
        Recipe map = modelMapper.map(dto, Recipe.class);

        Set<Diet> diets = dietService.getAllByIds(dto.getDietIds());
        Set<Ingredient> ingredients = ingredientService.getAllByIds(dto.getIngredientIds());
        User author = this.userService.getUserById(userId);

        map.setCreatedOn(Instant.now())
                .setModifiedOn(Instant.now())
                .setAuthor(this.userService.getUserById(userId))
                .setIngredients(ingredients)
                .setDiets(diets);

        return map;
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
