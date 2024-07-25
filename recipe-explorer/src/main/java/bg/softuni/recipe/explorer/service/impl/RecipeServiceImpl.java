package bg.softuni.recipe.explorer.service.impl;

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

    private RecipeShortInfoDTO mapToShort(Recipe entity) {



        RecipeShortInfoDTO map = modelMapper.map(entity, RecipeShortInfoDTO.class);

        return map;
    }

    private static class IngredientToNameListConverter extends AbstractConverter<Set<Ingredient>, List<String>> {

        @Override
        protected List<String> convert(Set<Ingredient> src) {

            return src.stream()
                    .map(Ingredient::getName)
                    .toList();
        }
    }

    private static class DietToTypeListConverter extends AbstractConverter<Set<Diet>, List<DietaryType>> {

        @Override
        protected List<DietaryType> convert(Set<Diet> source) {

            return source.stream()
                    .map(Diet::getType)
                    .toList();
        }
    }
}
