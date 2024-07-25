package bg.softuni.recipe.explorer.service.init;

import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.entity.Recipe;
import bg.softuni.recipe.explorer.model.enums.MealType;
import bg.softuni.recipe.explorer.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class RecipeInitServiceImpl {

    private final static int RECIPE_INIT_COUNT = 10;
    private final static int INGREDIENT_BOUND = 6;
    private final static int DIET_BOUND = 3;
    private final static String LOREM_IPSUM = "Lorem ipsum odor amet, consectetuer adipiscing elit. Id proin ipsum nulla semper himenaeos potenti nisl donec. Eros ultrices massa vitae condimentum cursus ligula auctor ultricies. Torquent viverra libero ultricies porttitor penatibus neque bibendum netus sociosqu. Ipsum quis rhoncus nulla; quisque curabitur nullam. Eros leo neque finibus molestie lectus tellus.";

    private final RecipeRepository recipeRepository;
    private final IngredientInitServiceImpl ingredientInitService;
    private final UserInitServiceImpl userInitService;
    private final DietInitServiceImpl dietInitService;

    @Autowired
    public RecipeInitServiceImpl(
            RecipeRepository recipeRepository,
            IngredientInitServiceImpl ingredientInitService,
            UserInitServiceImpl userInitService,
            DietInitServiceImpl dietInitService
    ) {
        this.recipeRepository = recipeRepository;
        this.ingredientInitService = ingredientInitService;
        this.userInitService = userInitService;
        this.dietInitService = dietInitService;
    }

    @Transactional
    public void init() {
        if (this.recipeRepository.count() > 0) return;

        List<Recipe> newRecipes = new ArrayList<>();

        Random random = new Random();
        int intMealBound = MealType.values().length;

        for (int recipeSeq = 1; recipeSeq <= RECIPE_INIT_COUNT; recipeSeq++) {
            //TODO: hashcode equals for entity set collections

            Set<Ingredient> randomIngSet = ingredientInitService.getRandomCountOfIngredients(INGREDIENT_BOUND);

            Instant now = Instant.now();
            String name = "Recipe " + recipeSeq;

            newRecipes.add(new Recipe()
                    .setName(name)
                    .setAuthor(userInitService.getRandomUser())
                    .setIngredients(randomIngSet)
                    .setCreatedOn(now)
                    .setModifiedOn(now)
                    .setMealType(MealType.values()[random.nextInt(intMealBound)])
                    .setInstructions(name + "\n" + LOREM_IPSUM)
                    .setDiets(dietInitService.getRandomCountOfDiets(DIET_BOUND))
            );
        }

        this.recipeRepository.saveAll(newRecipes);
    }
}
