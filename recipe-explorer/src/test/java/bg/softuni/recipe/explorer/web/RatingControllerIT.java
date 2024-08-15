package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.entity.*;
import bg.softuni.recipe.explorer.model.enums.*;
import bg.softuni.recipe.explorer.repository.*;
import bg.softuni.recipe.explorer.service.init.RoleInitServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RatingControllerIT {

    private final static String BASE_URL = "http://localhost/ratings/put";
    private final static String USER_LOGIN_URL = "http://localhost/users/login";

    private User TEST_USER;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleInitServiceImpl roleInitService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    void init() {
        roleInitService.init();

        TEST_USER = this.userRepository.save(new User()
                .setUsername("existing")
                .setEmail("existing@user.com")
                .setFirstName("existingFirstName")
                .setLastName("existingLastName")
                .setPassword("password")
                .setRoles(Set.of(
                        this.roleRepository.findByName(RoleEnum.USER)
                                .orElseThrow())));

        this.userRepository.save(new User()
                .setUsername("admin")
                .setEmail("admin")
                .setPassword("admin")
                .setFirstName("name")
                .setLastName("last name")
                .setRoles(Set.of(this.roleRepository.findByName(RoleEnum.ADMIN).orElseThrow())));
    }

    @AfterEach
    void tearDown() {

        List<Long> userIds = this.userRepository.findAll()
                .stream()
                .map(User::getId)
                .toList();
        List<Long> ingIds = this.ingredientRepository.findAll()
                .stream()
                .map(Ingredient::getId)
                .toList();
        List<Long> recIds = this.recipeRepository.findAll()
                .stream()
                .map(Recipe::getId)
                .toList();

        this.recipeRepository.deleteAllById(recIds);
        this.ingredientRepository.deleteAllById(ingIds);
        this.userRepository.deleteAllById(userIds);
    }

    @Test
    @WithAnonymousUser
    void testPutRatingRedirectsLoginForAnonymous() throws Exception {
        mockMvc.perform(put(BASE_URL).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USER_LOGIN_URL));
    }

    @Test
    @WithUserDetails(
            value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testPutRatingRedirectsAfterInvalidData() throws Exception {
        mockMvc.perform(put(BASE_URL)
                        .param("recipeId", "")
                        .param("rating", "")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @Transactional
    @WithUserDetails(
            value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testPutRatingCreatesRatingForRecipeWithCorrectDataAndUpdatesAvgRating() throws Exception {

        Recipe recipe = this.recipeRepository.save(populateOneRecipe());
        mockMvc.perform(put(BASE_URL)
                        .param("recipeId", recipe.getId().toString())
                        .param("rating", RatingEnum.TWO.toString())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/" + recipe.getId()));

        List<Rating> actualRatingsRecipe = this.ratingRepository.findAllByRecipe_Id(recipe.getId());
        assertTrue(actualRatingsRecipe.size() == 1);
        Rating actualRating = actualRatingsRecipe.getFirst();
        assertEquals(recipe, actualRating.getRecipe());
        assertEquals(TEST_USER, actualRating.getUser());
        assertEquals(RatingEnum.TWO, actualRating.getRating());
        assertTrue(BigDecimal.valueOf(2)
                .compareTo(this.recipeRepository.findById(recipe.getId())
                                .orElseThrow()
                                .getAverageRating())
                == 0);
    }

    @Test
    @WithUserDetails(value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService")
    void testPutRatingUpdatesRatingForRecipeAndUpdatesAvgRating() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());
        Rating rating = this.ratingRepository.save(
                new Rating()
                        .setRating(RatingEnum.FIVE)
                        .setRecipe(recipe)
                        .setUser(TEST_USER)
        );

        mockMvc.perform(put(BASE_URL)
                        .param("recipeId", recipe.getId().toString())
                        .param("rating", RatingEnum.TWO.toString())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/" + recipe.getId()));

        Optional<Rating> ratingById = this.ratingRepository.findById(rating.getId());
        assertTrue(ratingById.isPresent());
        Rating actualRating = ratingById.get();
        assertEquals(RatingEnum.TWO, actualRating.getRating());
        Optional<Recipe> recipeById = this.recipeRepository.findById(recipe.getId());
        assertTrue(recipeById.isPresent());
        Recipe actualRecipe = recipeById.get();
        assertTrue(actualRecipe.getAverageRating().compareTo(BigDecimal.valueOf(2)) == 0);
    }

    @Test
    @WithUserDetails(value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService")
    void testPutRatingUpdatesAverageRating() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());
        User admin = this.userRepository.findByUsername("admin")
                .orElseThrow();
        this.ratingRepository.save(new Rating()
                .setRating(RatingEnum.FIVE)
                .setRecipe(recipe)
                .setUser(admin));

        mockMvc.perform(put(BASE_URL)
                .param("rating", RatingEnum.THREE.toString())
                .param("recipeId", recipe.getId().toString())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/" + recipe.getId()));

        Optional<Recipe> recipeById = this.recipeRepository.findById(recipe.getId());
        assertTrue(recipeById.isPresent());
        Recipe actualRecipe = recipeById.get();
        BigDecimal expectedAvgRating = (BigDecimal.valueOf(5).add(BigDecimal.valueOf(3)))
                .divide(BigDecimal.valueOf(2), 1, RoundingMode.HALF_UP);

        assertTrue(expectedAvgRating.compareTo(actualRecipe.getAverageRating()) == 0);
    }

    private List<Ingredient> persistTwoIngredients() {

        this.ingredientRepository.save(
                new Ingredient()
                        .setName("test ingredient 1")
                        .setIngredientType(IngredientType.BEVERAGE)
                        .setUnit(UnitEnum.GRAMS)
                        .setDescription("test2test2test2test2")
                        .setAddedBy(TEST_USER));

        this.ingredientRepository.save(
                new Ingredient()
                        .setName("test ingredient 2")
                        .setIngredientType(IngredientType.BEVERAGE)
                        .setUnit(UnitEnum.GRAMS)
                        .setDescription("test1test1test1test1")
                        .setAddedBy(TEST_USER));

        List<Ingredient> all = this.ingredientRepository.findAll();

        return all;
    }

    private Recipe populateOneRecipe() {
        List<Ingredient> ingredients = persistTwoIngredients();

        List<Recipe> generatedRecipes = recipeBasicGenerator(1);
        Recipe recipe = generatedRecipes.getFirst();
        recipe.setIngredients(new HashSet<>(ingredients))
                .setAuthor(TEST_USER)
                .setMealType(MealType.MAIN);

        return recipe;
    }

    private List<Recipe> recipeBasicGenerator(int count) {

        List<Recipe> recipes = new ArrayList<>();
        String baseString = "test";

        for (int i = 1; i <= count; i++) {
            String base = baseString + i;
            Instant now = Instant.now();

            recipes.add(
                    new Recipe()
                            .setName(base)
                            .setInstructions(base.repeat(9))
                            .setCreatedOn(now)
                            .setModifiedOn(now));
        }

        return recipes;
    }
}
