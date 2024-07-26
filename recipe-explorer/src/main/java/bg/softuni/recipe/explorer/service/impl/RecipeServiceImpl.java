package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.exceptions.ObjectNotFoundException;
import bg.softuni.recipe.explorer.model.dto.RecipeDetailsDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeShortInfoDTO;
import bg.softuni.recipe.explorer.model.entity.Diet;
import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.entity.Recipe;
import bg.softuni.recipe.explorer.model.enums.DietaryType;
import bg.softuni.recipe.explorer.repository.RecipeRepository;
import bg.softuni.recipe.explorer.service.RecipeService;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RecipeServiceImpl(
            RecipeRepository recipeRepository, ModelMapper modelMapper
    ) {
        this.recipeRepository = recipeRepository;
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



    private RecipeShortInfoDTO mapToShort(Recipe entity) {
        RecipeShortInfoDTO map = modelMapper.map(entity, RecipeShortInfoDTO.class);
        return map;
    }

    private RecipeDetailsDTO mapToDetails(Recipe entity) {
        RecipeDetailsDTO map = modelMapper.map(entity, RecipeDetailsDTO.class);
        return map;
    }
}
