package bg.softuni.recipe.explorer.config;

import bg.softuni.recipe.explorer.model.dto.RecipeAddDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeDetailsDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeEditDTO;
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
        Converter<Set<Diet>, List<String>> dietToNameListConverter = new DietToTypeListConverter();
        Converter<Set<Diet>, List<Long>> dietToIdListConverter = new DietToIdListConverter();


//        TypeMap Recipe -> RecipeShortDTO
        TypeMap<Recipe, RecipeShortInfoDTO> typeMapToRecipeShort =
                modelMapper.typeMap(Recipe.class, RecipeShortInfoDTO.class);


        typeMapToRecipeShort.addMappings(mapper -> mapper.using(ingredientToNameListConverter)
                        .map(Recipe::getIngredients, RecipeShortInfoDTO::setIngredientNames))
                .addMappings(mapper -> mapper.using(dietToNameListConverter)
                        .map(Recipe::getDiets, RecipeShortInfoDTO::setDietTypes));

//        TypeMap Recipe -> RecipeDetailsDTO
        TypeMap<Recipe, RecipeDetailsDTO> typeMapToRecipeDetails =
                modelMapper.typeMap(Recipe.class, RecipeDetailsDTO.class);

        typeMapToRecipeDetails.addMappings(mapper -> mapper.using(ingredientToNameListConverter)
                        .map(Recipe::getIngredients, RecipeDetailsDTO::setIngredientNames))
                .addMappings(mapper -> mapper.using(dietToNameListConverter)
                        .map(Recipe::getDiets, RecipeDetailsDTO::setDietaryTypes));

//        TypeMap Recipe -> RecipeAddDTO
        TypeMap<Recipe, RecipeAddDTO> typeMapToRecipeAdd =
                modelMapper.typeMap(Recipe.class, RecipeAddDTO.class);

        typeMapToRecipeAdd.addMappings(mapper -> mapper.using(dietToIdListConverter)
                .map(Recipe::getDiets, RecipeAddDTO::setDietIds));

//        TypeMap Recipe -> RecipeEditDTO
        TypeMap<Recipe, RecipeEditDTO> typeMapToRecipeEdit =
                modelMapper.typeMap(Recipe.class, RecipeEditDTO.class);

        typeMapToRecipeEdit.addMappings(mapper -> mapper.using(dietToIdListConverter)
                .map(Recipe::getDiets, RecipeEditDTO::setDietIds));


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

    private static class DietToIdListConverter extends AbstractConverter<Set<Diet>, List<Long>> {

        @Override
        protected List<Long> convert(Set<Diet> diets) {

            return diets.stream()
                    .map(Diet::getId)
                    .toList();
        }
    }
}
