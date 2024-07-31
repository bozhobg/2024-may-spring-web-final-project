package bg.softuni.recipe.explorer.config;

import bg.softuni.recipe.explorer.model.dto.RecipeDetailsDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeShortInfoDTO;
import bg.softuni.recipe.explorer.model.entity.Diet;
import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.entity.Recipe;
import bg.softuni.recipe.explorer.utils.StringFormatter;
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

        Converter<Set<Ingredient>, List<String>> ingredientToNameListConverter = new IngredientToNameListConverter();
        Converter<Set<Diet>, List<String>> dietToTypeListConverter = new DietToTypeListConverter();

//        TypeMap Recipe -> RecipeShortDTO
        TypeMap<Recipe, RecipeShortInfoDTO> typeMapToRecipeShort =
                modelMapper.typeMap(Recipe.class, RecipeShortInfoDTO.class);


        typeMapToRecipeShort.addMappings(mapper -> mapper.using(ingredientToNameListConverter)
                        .map(Recipe::getIngredients, RecipeShortInfoDTO::setIngredientNames))
                .addMappings(mapper -> mapper.using(dietToTypeListConverter)
                        .map(Recipe::getDiets, RecipeShortInfoDTO::setDietaryTypes));

//        TypeMap Recipe -> RecipeDetailsDTO
        TypeMap<Recipe, RecipeDetailsDTO> typeMapToRecipeDetails =
                modelMapper.typeMap(Recipe.class, RecipeDetailsDTO.class);

        typeMapToRecipeDetails.addMappings(mapper -> mapper.using(ingredientToNameListConverter)
                        .map(Recipe::getIngredients, RecipeDetailsDTO::setIngredientNames))
                .addMappings(mapper -> mapper.using(dietToTypeListConverter)
                        .map(Recipe::getDiets, RecipeDetailsDTO::setDietaryTypes));

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

    private static class DietToTypeListConverter extends AbstractConverter<Set<Diet>, List<String>> {

        @Override
        protected List<String> convert(Set<Diet> source) {

            return source.stream()
                    .map(e -> StringFormatter.mapConstantCaseToUpperCase(e.getDietaryType().name()))
                    .toList();
        }
    }
}
