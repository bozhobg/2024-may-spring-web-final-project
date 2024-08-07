package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.dto.RecipeShortInfoDTO;
import bg.softuni.recipe.explorer.model.entity.Diet;
import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.entity.Recipe;
import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.model.enums.*;
import bg.softuni.recipe.explorer.repository.*;
import bg.softuni.recipe.explorer.service.init.RoleInitServiceImpl;
import bg.softuni.recipe.explorer.utils.StringFormatter;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerIT {

    private final static String BASE_URL = "http://localhost/recipes";
    private final static String USER_LOGIN_URL = "http://localhost/users/login";


    private User TEST_USER;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoleInitServiceImpl roleInitService;
    @Autowired
    private DietRepository dietRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
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

    }

    @AfterEach
    void tearDown() {

        List<Long> userIds = this.userRepository.findAll()
                .stream()
                .map(User::getId)
                .toList();
        List<Long> dietIds = this.dietRepository.findAll()
                .stream()
                .map(Diet::getId)
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
        this.dietRepository.deleteAllById(dietIds);
        this.userRepository.deleteAllById(userIds);
    }

    @Test
    @WithAnonymousUser
    void testGetAddRedirect_ForAnonymous() throws Exception {
        mockMvc.perform(get(BASE_URL + "add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USER_LOGIN_URL));
    }

    @Test
    @WithUserDetails(value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService")
    void testGetAdd() throws Exception {
        mockMvc.perform(get(BASE_URL + "/add"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService")
    void testPostAddRedirect_ForEmptyInputs() throws Exception {
//        TODO: individual field validation

        mockMvc.perform(post(BASE_URL + "/add")
                        .param("name", "")
                        .param("instructions", "")
                        .param("mealType", "")
                        .param("ingredientIds", "")
                        .param("dietIds", "")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/add"));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService")
    void testPostAddRedirectsToCreatedRecipe() throws Exception {

//        Arrange

        String testName = "test recipe";
        String testInstructions = "test test test test test test test test test test test test";
        String mealTypeString = MealType.APPETIZER.toString();

        List<Diet> diets = persistTwoDiets();
        String dietIds = diets.stream()
                .map(d -> d.getId().toString())
                .collect(Collectors.joining(","));

        List<Ingredient> ingredients = persistTwoIngredients();
        String ingredientIds = ingredients.stream()
                .map(d -> d.getId().toString())
                .collect(Collectors.joining(","));
//        Act
//        Assert

        ResultActions perform = mockMvc.perform(post(BASE_URL + "/add")
                .param("name", testName)
                .param("instructions", testInstructions)
                .param("mealType", mealTypeString)
                .param("ingredientIds", ingredientIds)
                .param("dietIds", dietIds)
                .with(csrf())
        );

        Optional<Recipe> byId = this.recipeRepository.findByName(testName);
        assertTrue(byId.isPresent());
        Recipe recipe = byId.get();

        perform.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/" + recipe.getId()));


        assertEquals(testName, recipe.getName());
        assertEquals(testInstructions, recipe.getInstructions());
        assertEquals(mealTypeString, recipe.getMealType().toString());

        assertEquals(ingredients,
                recipe.getIngredients().stream()
                        .sorted(Comparator.comparing(Ingredient::getId))
                        .toList());
        assertEquals(diets,
                recipe.getDiets().stream()
                        .sorted(Comparator.comparing(Diet::getId))
                        .toList());
    }

    @Test
    @WithAnonymousUser
    void testGetAllRedirects_ForAnonymous() throws Exception {

        mockMvc.perform(get(BASE_URL + "/all"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USER_LOGIN_URL));
    }

    @Test
    @WithUserDetails(value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService")
    void testGetAllReturnsPersisted() throws Exception {
//        Arrange
        List<Diet> diets = persistTwoDiets();
        List<Ingredient> ingredients = persistTwoIngredients();
        List<Recipe> recipes = recipeGenerator(3);

        recipes.get(0)
                .setIngredients(Set.of(ingredients.get(0)))
                .setDiets(Set.of(diets.get(0)))
                .setAverageRating(BigDecimal.ONE)
                .setAuthor(TEST_USER);
        recipes.get(1)
                .setIngredients(Set.of(ingredients.get(1)))
                .setDiets(Set.of(diets.get(0)))
                .setAverageRating(BigDecimal.TWO)
                .setAuthor(TEST_USER);
        recipes.get(2)
                .setIngredients(new HashSet<>(ingredients))
                .setDiets(new HashSet<>(diets))
                .setAverageRating(BigDecimal.valueOf(5))
                .setAuthor(TEST_USER);

        this.recipeRepository.saveAll(recipes);

//        Act
//        Assert
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL + "/all"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        assertNotNull(mav);
        assertTrue(mav.hasView());
        assertEquals("recipes-all", mav.getViewName());
        Map<String, Object> model = mav.getModel();
        assertTrue(model.containsKey("all"));
        List<RecipeShortInfoDTO> all = (List<RecipeShortInfoDTO>) model.get("all");
        assertEquals(recipes.size(), all.size());

        for (RecipeShortInfoDTO dto : all) {
            Long id = dto.getId();
            Optional<Recipe> first = recipes.stream().filter(r -> r.getId().equals(id)).findFirst();
            assertTrue(first.isPresent());
            Recipe actual = first.get();

            assertEquals(actual.getName(), dto.getName());
            assertEquals(actual.getMealType(), dto.getMealType());

            Set<Ingredient> actualIngs = actual.getIngredients();
            List<String> expectedIngNames = dto.getIngredientNames();
            assertEquals(actualIngs.size(), expectedIngNames.size());

            for (Ingredient actualIng : actualIngs) {
                assertTrue(expectedIngNames.contains(actualIng.getName()));
            }

            Set<Diet> actualDiets = actual.getDiets();
            List<DietaryType> expectedDietTypes = dto.getDietTypes();
            assertEquals(actualDiets.size(), expectedDietTypes.size());

            System.out.println(actualDiets);
            System.out.println(expectedDietTypes);

//            TODO: Research -> actually takes manipulated data at template level?

            for (Diet actualDiet : actualDiets) {

                assertTrue(expectedDietTypes.contains(
                        StringFormatter.mapConstantCaseToUpperCase(
                                actualDiet.getDietaryType().toString()))
                );
            }

//            BigDecimal ignore scaling with compareTo
            assertTrue(dto.getAverageRating().compareTo(actual.getAverageRating()) == 0);
        }
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

    private List<Diet> persistTwoDiets() {

        return this.dietRepository.saveAll(List.of(
                new Diet().setDescription("Diet description 1")
                        .setDietaryType(DietaryType.KETO),
                new Diet().setDescription("Diet description 2")
                        .setDietaryType(DietaryType.DAIRY_INTOLERANT)));
    }

    private List<Recipe> recipeGenerator(int count) {

        List<Recipe> recipes = new ArrayList<>();
        String baseString = "test";

        for (int i = 1; i <= count; i++) {
            String base = baseString + i;
            Instant now = Instant.now();

            recipes.add(new Recipe()
                    .setName(base)
                    .setInstructions(base.repeat(9))
                    .setCreatedOn(now)
                    .setModifiedOn(now));
        }

        return recipes;
    }
}
