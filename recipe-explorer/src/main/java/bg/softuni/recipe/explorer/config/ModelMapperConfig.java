package bg.softuni.recipe.explorer.config;

import bg.softuni.recipe.explorer.model.dto.RecipeShortInfoDTO;
import bg.softuni.recipe.explorer.model.entity.Diet;
import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.entity.Recipe;
import bg.softuni.recipe.explorer.model.enums.DietaryType;
import bg.softuni.recipe.explorer.service.impl.RecipeServiceImpl;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

//        TypeMap Recipe to RecipeShortDTO
        TypeMap<Recipe, RecipeShortInfoDTO> typeMap = modelMapper.typeMap(Recipe.class, RecipeShortInfoDTO.class);

        Converter<Set<Ingredient>, List<String>> ingredientToNameListConverter = new IngredientToNameListConverter();
        Converter<Set<Diet>, List<DietaryType>> dietToTypeListConverter = new DietToTypeListConverter();

        typeMap.addMappings(mapper -> mapper.using(ingredientToNameListConverter)
                        .map(Recipe::getIngredients, RecipeShortInfoDTO::setIngredientNames))
                .addMappings(mapper -> mapper.using(dietToTypeListConverter)
                        .map(Recipe::getDiets, RecipeShortInfoDTO::setDietTypes));

        return modelMapper;
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
