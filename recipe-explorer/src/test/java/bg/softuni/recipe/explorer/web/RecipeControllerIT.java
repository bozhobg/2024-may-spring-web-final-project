package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.constants.SortingEnum;
import bg.softuni.recipe.explorer.model.dto.RecipeDetailsDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeEditDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeShortInfoDTO;
import bg.softuni.recipe.explorer.model.entity.Diet;
import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.entity.Recipe;
import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.model.enums.*;
import bg.softuni.recipe.explorer.repository.*;
import bg.softuni.recipe.explorer.service.init.RoleInitServiceImpl;
import bg.softuni.recipe.explorer.utils.AverageRatingComparator;
import bg.softuni.recipe.explorer.utils.StringFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        this.userRepository.save(new User()
                .setUsername("unauthorized")
                .setEmail("unauthorized")
                .setPassword("unauthorized")
                .setFirstName("name")
                .setLastName("last name")
                .setRoles(Set.of(this.roleRepository.findByName(RoleEnum.USER).orElseThrow())));

        this.userRepository.save(new User()
                .setUsername("moderator")
                .setEmail("moderator")
                .setPassword("moderator")
                .setFirstName("name")
                .setLastName("last name")
                .setRoles(Set.of(this.roleRepository.findByName(RoleEnum.MODERATOR).orElseThrow())));

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
    @WithUserDetails(
            value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService")
    void testGetAllReturnsPersisted() throws Exception {

//        Arrange
        List<Recipe> recipes = populateThreeRecipesWithRelations();
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
            List<String> expectedDietTypes = dto.getDietTypes();
            assertEquals(actualDiets.size(), expectedDietTypes.size());

            //            TODO: Research -> actually takes manipulated data at template level?

            for (Diet actualDiet : actualDiets) {

                assertTrue(
                        expectedDietTypes.contains(
                                StringFormatter.mapConstantCaseToUpperCase(actualDiet.getDietaryType().toString())));
            }

//            BigDecimal ignore scaling with compareTo
            assertTrue(dto.getAverageRating().compareTo(actual.getAverageRating()) == 0);
        }
    }

    @Test
    @WithUserDetails(
            value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService")
    void testFilterReturnsByCriteriaMealType() throws Exception {

//        Arrange
        List<Recipe> recipes = populateThreeRecipesWithRelations();
        this.recipeRepository.saveAll(recipes);

//        Act
//        Assert
        MvcResult byMealType = mockMvc.perform(get(BASE_URL + "/filter")
                        .queryParam("mealType", MealType.APPETIZER.toString()))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = byMealType.getModelAndView();
        assertNotNull(mav);
        assertTrue(mav.hasView());
        assertEquals("recipes-all", mav.getViewName());
        assertTrue(mav.getModel().containsKey("all"));

        List<RecipeShortInfoDTO> actualAll = (List<RecipeShortInfoDTO>) mav.getModel().get("all");
        assertNotNull(actualAll);
        List<Recipe> expectedAll = this.recipeRepository.findAllByMealType(MealType.APPETIZER);
        assertEquals(1, expectedAll.size());
        assertEquals(1, actualAll.size());

        Recipe expectedRecipe = expectedAll.get(0);
        RecipeShortInfoDTO actualDTO = actualAll.get(0);

        assertEquals(expectedRecipe.getId(), actualDTO.getId());
        assertEquals(expectedRecipe.getName(), actualDTO.getName());
        assertEquals(expectedRecipe.getMealType(), actualDTO.getMealType());
    }

    @Transactional
    @Test
    @WithUserDetails(
            value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService")
    void testFilterReturnsByCriteriaDietaryPreference() throws Exception {

//        Arrange
        List<Recipe> recipes = populateThreeRecipesWithRelations();
        this.recipeRepository.saveAll(recipes);
        Optional<Diet> byDietaryType = this.dietRepository.findByDietaryType(DietaryType.KETO);
        assertTrue(byDietaryType.isPresent());
        Diet diet = byDietaryType.get();

//        Act
//        Assert

        MvcResult byDietId = mockMvc.perform(get(BASE_URL + "/filter")
                        .queryParam("dietId", diet.getId().toString())
                ).andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = byDietId.getModelAndView();
        assertNotNull(mav);
        assertTrue(mav.hasView());
        assertEquals("recipes-all", mav.getViewName());
        assertTrue(mav.getModel().containsKey("all"));

        List<RecipeShortInfoDTO> actualAll = (List<RecipeShortInfoDTO>) mav.getModel().get("all");
        assertNotNull(actualAll);
        List<Recipe> expectedAll = this.recipeRepository.findAllByDietsContaining(diet);
        assertNotNull(expectedAll);
        assertEquals(expectedAll.size(), actualAll.size());

        for (RecipeShortInfoDTO dto : actualAll) {

            Optional<Recipe> first = expectedAll.stream()
                    .filter(r -> r.getId().equals(dto.getId()))
                    .findFirst();
            assertTrue(first.isPresent());
            Recipe expected = first.get();

            assertEquals(expected.getId(), dto.getId());
            assertEquals(expected.getName(), dto.getName());
            Set<Diet> expectedDiets = expected.getDiets();
//            TODO: Actual list content is Strings
            List<String> actualDietsString = dto.getDietTypes();

            assertEquals(expectedDiets.size(), actualDietsString.size());
            for (Diet expectedDiet : expectedDiets) {
                String expectedTypeFormatted = StringFormatter.mapConstantCaseToUpperCase(expectedDiet.getDietaryType().toString());
                assertTrue(actualDietsString.contains(expectedTypeFormatted));
            }
        }
    }

    @Transactional
    @Test
    @WithUserDetails(
            value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService")
    void testFilterReturnsAllOrderedByRating() throws Exception {

        List<Recipe> recipes = populateThreeRecipesWithRelations();

        recipes = this.recipeRepository.saveAll(recipes);
        recipes.sort(new AverageRatingComparator());
        recipes = recipes.reversed();
        List<Long> expectedIndices = recipes.stream()
                .map(Recipe::getId)
                .toList();

        MvcResult raringSort = mockMvc.perform(get(BASE_URL + "/filter")
                        .queryParam("ratingSort", SortingEnum.DESC.toString()))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = raringSort.getModelAndView();
        assertNotNull(mav);

        List<RecipeShortInfoDTO> all = (List<RecipeShortInfoDTO>) mav.getModel().get("all");
        assertNotNull(all);
        List<Long> actualIndices = all.stream()
                .map(RecipeShortInfoDTO::getId)
                .toList();

        assertArrayEquals(expectedIndices.toArray(), actualIndices.toArray());
    }

    @Test
    @WithAnonymousUser
    void testGetDetailsRedirectsForAnonymous() throws Exception {

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USER_LOGIN_URL));
    }

    @Transactional
    @Test
    @WithUserDetails(value = "existing",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetDetailsReturnsCorrectRecipeAndData() throws Exception {

        Recipe expectedRecipe = this.recipeRepository.save(populateOneRecipe());
        Long id = expectedRecipe.getId();

        ModelAndView mav = mockMvc.perform(get(BASE_URL + "/" + id))
                .andExpect(status().isOk())
                .andReturn()
                .getModelAndView();

        assertNotNull(mav);
        assertEquals("recipe-details", mav.getViewName());
        Map<String, Object> model = mav.getModel();
        assertTrue(model.containsKey("recipe"));
        assertTrue(model.containsKey("ratings"));
        assertTrue(model.containsKey("comments"));
        assertTrue(model.containsKey("editComment"));
        assertTrue(model.containsKey("userRating"));

        RecipeDetailsDTO actual = (RecipeDetailsDTO) model.get("recipe");
        assertEquals(expectedRecipe.getName(), actual.getName());
        assertEquals(expectedRecipe.getInstructions(), actual.getInstructions());
        assertEquals(expectedRecipe.getMealType(), actual.getMealType());
        assertEquals(expectedRecipe.getCreatedOn(), actual.getCreatedOn());
        assertEquals(expectedRecipe.getModifiedOn(), actual.getModifiedOn());
        assertEquals(expectedRecipe.getAuthor().getUsername(), actual.getAuthorUsername());

        Set<Ingredient> expIngredients = expectedRecipe.getIngredients();
        List<String> actIngNames = actual.getIngredientNames();
        assertEquals(expIngredients.size(), actIngNames.size());

        for (Ingredient expIng : expIngredients) {
            assertTrue(actIngNames.contains(expIng.getName()));
        }

        Set<Diet> expDiets = expectedRecipe.getDiets();
        List<String> actDietTypes = actual.getDietaryTypes();
        assertEquals(expDiets.size(), actDietTypes.size());

        for (Diet expDiet : expDiets) {
            assertTrue(actDietTypes.contains(
                    StringFormatter.mapConstantCaseToUpperCase(expDiet.getDietaryType().toString())));
        }

        assertTrue(expectedRecipe.getAverageRating().compareTo(actual.getAverageRating()) == 0);
    }


    @Test
    @WithUserDetails(value = "existing",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testGetDetailsForInvalidId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + 99))
                .andExpect(status().is(404));
    }

    @Test
    @WithAnonymousUser
    void testDeleteRecipeForAnonymous() throws Exception {

        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        mockMvc.perform(delete(BASE_URL + "/" + recipe.getId()))
                .andExpect(status().is(403));

    }

    @Test
    @WithUserDetails(
            value = "unauthorized",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testDeleteForUnauthorized() throws Exception {

        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        mockMvc.perform(delete(BASE_URL + "/" + recipe.getId())
                .with(csrf())
        ).andExpect(status().is(403));
    }

    @Test
    @WithUserDetails(
            value = "moderator",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testDeleteForModerator() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        mockMvc.perform(delete(BASE_URL + "/" + recipe.getId())
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/all"));

//        verify recipe deleted
        assertFalse(this.recipeRepository.existsById(recipe.getId()));
    }

    @Test
    @WithUserDetails(
            value = "admin",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testDeleteForAdmin() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        mockMvc.perform(delete(BASE_URL + "/" + recipe.getId())
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/all"));

        assertFalse(this.recipeRepository.existsById(recipe.getId()));
    }

    @Test
    @WithUserDetails(
            value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testDeleteForAuthor() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        mockMvc.perform(delete(BASE_URL + "/" + recipe.getId())
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/all"));

        assertFalse(this.recipeRepository.existsById(recipe.getId()));
    }

    @Test
    @WithUserDetails(
            value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testDeleteForNonExistingRecipeRedirects() throws Exception {

        mockMvc.perform(delete(BASE_URL + "/" + 99)
                .with(csrf())
        ).andExpect(status().is(404));
    }

    @Test
    @WithUserDetails(
            value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testGetRecipeEditReturnsErrorForNonExisting() throws Exception {

        mockMvc.perform(get(BASE_URL + "/" + 99 + "/edit"))
                .andExpect(status().is(404));
    }

    @Test
    @WithAnonymousUser
    void testGetRecipeEditRedirectsForAnonymous() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + 99 + "/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USER_LOGIN_URL));
    }

    @Test
    @WithUserDetails(
            value = "unauthorized",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testGetRecipeEditReturnsErrorForUnauthorized() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        mockMvc.perform(get(BASE_URL + "/" + recipe.getId() + "/edit"))
                .andExpect(status().is(403));
    }

    @Test
    @WithUserDetails(
            value = "moderator",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testGetRecipeEditIsOkForModerator() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        mockMvc.perform(get(BASE_URL + "/" + recipe.getId() + "/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(
            value = "admin",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testGetRecipeEditIsOkForAdmin() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        mockMvc.perform(get(BASE_URL + "/" + recipe.getId() + "/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(
            value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testGetRecipeEditIsOkForOwner() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        mockMvc.perform(get(BASE_URL + "/" + recipe.getId() + "/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @WithUserDetails(
            value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testGetRecipeEditReturnsCorrectData() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        MvcResult mvcResult = mockMvc.perform(get(BASE_URL + "/" + recipe.getId() + "/edit"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        assertNotNull(mav);
        assertEquals("recipe-edit", mav.getViewName());
        Map<String, Object> model = mav.getModel();

        assertTrue(model.containsKey("recipeId"));
        assertEquals(recipe.getId(), model.get("recipeId"));

        assertTrue(model.containsKey("recipeEditData"));
        assertInstanceOf(RecipeEditDTO.class, model.get("recipeEditData"));
        RecipeEditDTO actualData = (RecipeEditDTO) model.get("recipeEditData");

        assertEquals(recipe.getName(), actualData.getName());
        assertEquals(recipe.getInstructions(), actualData.getInstructions());
        assertEquals(recipe.getMealType(), actualData.getMealType());

        Long[] expectedDietIdsSorted = recipe.getDiets().stream()
                .map(Diet::getId)
                .sorted()
                .toArray(Long[]::new);
        Long[] actualDietIdsSorted = actualData.getDietIds().stream()
                .sorted()
                .toArray(Long[]::new);
        assertArrayEquals(expectedDietIdsSorted, actualDietIdsSorted);

//        ingredients set over js with fetch through ingredients rest controller
    }

    @Test
    @WithAnonymousUser
    void testPutRecipeRedirectsForAnonymous() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        mockMvc.perform(put(BASE_URL + "/" + recipe.getId() + "/edit"))
                .andExpect(status().is(403));
    }

    @Test
    @WithUserDetails(
            value = "unauthorized",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testPutRecipeReturnsErrorForUnauthorized() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        mockMvc.perform(put(BASE_URL + "/" + recipe.getId() + "/edit"))
                .andExpect(status().is(403));
    }

    @Test
    @WithUserDetails(
            value = "moderator",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testPutRecipeRedirectsForModeratorAndInvalidData() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        mockMvc.perform(put(BASE_URL + "/" + recipe.getId() + "/edit")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/" + recipe.getId() + "/edit"));
    }

    @Test
    @WithUserDetails(
            value = "admin",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testPutRecipeRedirectsForAdminAndInvalidData() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        mockMvc.perform(put(BASE_URL + "/" + recipe.getId() + "/edit")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/" + recipe.getId() + "/edit"));
    }

    @Test
    @WithUserDetails(
            value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testPutRecipeRedirectsForAuthorAndInvalidData() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        mockMvc.perform(put(BASE_URL + "/" + recipe.getId() + "/edit")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/" + recipe.getId() + "/edit"));
    }

    @Test
    @WithUserDetails(
            value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testPutRecipeRedirectsForInvalidRecipeAndData() throws Exception {

        mockMvc.perform(put(BASE_URL + "/" + 99 + "/edit")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/" + 99 + "/edit"));
    }

    @Test
    @Transactional
    @WithUserDetails(
            value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testPutUpdatesData() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        String expectedName = "updated";
        String expectedInstructions = "instructions instructions instructions instructions instructions ";
        Ingredient expectedIngredient = this.ingredientRepository.findAll().stream().findFirst().orElseThrow();
        Diet expectedDiet = this.dietRepository.findAll().stream().findFirst().orElseThrow();
        MealType expectedMealType = MealType.SALAD;

        mockMvc.perform(put(BASE_URL + "/" + recipe.getId() + "/edit")
                        .param("name", expectedName)
                        .param("instructions", expectedInstructions)
                        .param("mealType", String.valueOf(MealType.SALAD))
                        .param("ingredientIds", expectedIngredient.getId().toString())
                        .param("dietIds", expectedDiet.getId().toString())
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/" + recipe.getId()));

        Optional<Recipe> byId = this.recipeRepository.findById(recipe.getId());
        assertTrue(byId.isPresent());
        Recipe actualRecipe = byId.get();
        assertEquals(expectedName, actualRecipe.getName());
        assertEquals(expectedInstructions, actualRecipe.getInstructions());
        assertEquals(expectedMealType, actualRecipe.getMealType());
        assertTrue(actualRecipe.getIngredients().size() == 1);
        assertTrue(actualRecipe.getIngredients().stream()
                .anyMatch(i -> i.getId().equals(expectedIngredient.getId())));
        assertTrue(actualRecipe.getDiets().size() == 1);
        assertTrue(actualRecipe.getDiets().stream()
                .anyMatch(d -> d.getId().equals(expectedDiet.getId())));

    }

    @Test
    @Transactional
    @WithUserDetails(
            value = "existing",
            setupBefore = TestExecutionEvent.TEST_EXECUTION,
            userDetailsServiceBeanName = "userDetailsService"
    )
    void testPutRedirectsForInvalidData() throws Exception {
        Recipe recipe = this.recipeRepository.save(populateOneRecipe());

        mockMvc.perform(put(BASE_URL + "/" + recipe.getId() + "/edit")
                        .param("name", "")
                        .param("instructions", "")
                        .param("mealType", "")
                        .param("ingredientIds", "")
                        .param("dietIds", "")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/" + recipe.getId() + "/edit"));
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

    private Recipe populateOneRecipe() {
        List<Diet> diets = persistTwoDiets();
        List<Ingredient> ingredients = persistTwoIngredients();

        List<Recipe> generatedRecipes = recipeBasicGenerator(1);
        Recipe recipe = generatedRecipes.getFirst();
        recipe.setIngredients(new HashSet<>(ingredients))
                .setDiets(new HashSet<>(diets))
                .setAverageRating(BigDecimal.TWO)
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

    private List<Recipe> populateThreeRecipesWithRelations() {

        List<Diet> diets = persistTwoDiets();
        List<Ingredient> ingredients = persistTwoIngredients();
        List<Recipe> recipes = recipeBasicGenerator(3);

        recipes.get(0)
                .setIngredients(Set.of(ingredients.get(0)))
                .setDiets(Set.of(diets.get(0)))
                .setAverageRating(BigDecimal.valueOf(3))
                .setAuthor(TEST_USER)
                .setMealType(MealType.APPETIZER);

        recipes.get(1)
                .setIngredients(Set.of(ingredients.get(1)))
                .setDiets(Set.of(diets.get(1)))
                .setAverageRating(BigDecimal.ONE)
                .setAuthor(TEST_USER)
                .setMealType(MealType.MAIN);

        recipes.get(2)
                .setIngredients(new HashSet<>(ingredients))
                .setDiets(new HashSet<>(diets))
                .setAverageRating(BigDecimal.TWO)
                .setAuthor(TEST_USER)
                .setMealType(MealType.SOUP);

        return recipes;
    }
}
