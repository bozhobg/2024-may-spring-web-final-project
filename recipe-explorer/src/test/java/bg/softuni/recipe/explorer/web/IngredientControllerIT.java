package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.dto.IngredientDetailsDTO;
import bg.softuni.recipe.explorer.model.dto.IngredientShortInfoDTO;
import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.entity.Role;
import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.model.enums.IngredientType;
import bg.softuni.recipe.explorer.model.enums.RoleEnum;
import bg.softuni.recipe.explorer.model.enums.UnitEnum;
import bg.softuni.recipe.explorer.repository.IngredientRepository;
import bg.softuni.recipe.explorer.repository.RoleRepository;
import bg.softuni.recipe.explorer.repository.UserRepository;
import bg.softuni.recipe.explorer.service.IngredientService;
import bg.softuni.recipe.explorer.service.UserService;
import bg.softuni.recipe.explorer.service.init.RoleInitServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IngredientControllerIT {

    private final static String BASE_URL = "http://localhost";
    private final static String USER_LOGIN_URL = BASE_URL + "/users/login";
    
    private final static Ingredient NEW_INGREDIENT =
            new Ingredient()
                    .setName("Ostrich")
                    .setUnit(UnitEnum.GRAMS)
                    .setIngredientType(IngredientType.EGG)
                    .setDescription("Lorem ipsum odor amet, consectetuer adipiscing elit. Dictumst nascetur maximus nibh nascetur ad magna euismod rhoncus suspendisse mus eros.");

    private User TEST_USER;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleInitServiceImpl roleInitService;
    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    void init() {
//        TODO: Disable DbInit

        roleInitService.init();

        TEST_USER = new User()
                .setUsername("existing")
                .setEmail("existing@user.com")
                .setFirstName("existingFirstName")
                .setLastName("existingLastName")
                .setPassword("password")
                .setRoles(Set.of(
                        this.roleRepository.findByName(RoleEnum.USER)
                                .orElseThrow()));

        TEST_USER = this.userRepository.save(TEST_USER);
    }

    //        TODO: getting constraint violation on tearDown of repositories
//        TODO: HsqlException for on this op, constraint violation on deleteAll repo operations

    @AfterEach
    void tearDown() {
//        TODO: Disable DbInit
//        TODO: impl hash and equals on entities, research
//        Relating to issue by colleague to lucho deleting with owned entity
//        -> impl of hashcode equals (not easy) or separating owned entity
//        FIX: delete by unique identifier not by reference ??? .deleteAll() throws

        List<Long> ingredientIds = this.ingredientRepository.findAll()
                .stream()
                .map(Ingredient::getId)
                .toList();
        List<Long> userIds = this.userRepository.findAll()
                .stream()
                .map(User::getId)
                .toList();

        this.ingredientRepository.deleteAllById(ingredientIds);
        this.userRepository.deleteAllById(userIds);
    }

    @Test
    @WithUserDetails(
            value = "existing",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testGetAdd() throws Exception {
        mockMvc.perform(get("/ingredients/add"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testGetAddRedirects_ForAnonymous() throws Exception {
        mockMvc.perform(get("/ingredients/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USER_LOGIN_URL));
    }

    @Test
    @WithUserDetails(
            value = "existing",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testPostAddRedirects_ForInvalidInput() throws Exception {

        mockMvc.perform(post("/ingredients/add")
                        .param("name", "")
                        .param("description", "")
                        .param("unit", "")
                        .param("type", "")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ingredients/add"));
    }

    @Test
    @WithUserDetails(
            value = "existing",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testPostAddRedirects_ForValidInput() throws Exception {
//        Act
        ResultActions resultActions = mockMvc.perform(post("/ingredients/add")
                        .param("name", NEW_INGREDIENT.getName())
                        .param("description", NEW_INGREDIENT.getDescription())
                        .param("unit", NEW_INGREDIENT.getUnit().toString())
                        .param("type", NEW_INGREDIENT.getIngredientType().toString())
                        .with(csrf()))
//                Assert
                .andExpect(status().is3xxRedirection());

        Optional<Ingredient> optIngredient = this.ingredientRepository.findByName(NEW_INGREDIENT.getName());
        assertTrue(optIngredient.isPresent());

        Ingredient actual = optIngredient.get();

        resultActions.andExpect(redirectedUrl("/ingredients/" + actual.getId()));
        assertEquals(NEW_INGREDIENT.getDescription(), actual.getDescription());
        assertEquals(NEW_INGREDIENT.getUnit(), actual.getUnit());
        assertEquals(NEW_INGREDIENT.getIngredientType(), actual.getIngredientType());
        assertEquals("existing", actual.getAddedBy().getUsername());
    }

    @Test
    @WithAnonymousUser
    void getAllRedirects_ForAnonymous() throws Exception {
        mockMvc.perform(get("/ingredients/all"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USER_LOGIN_URL));
    }

    @Test
    @WithUserDetails(
            value = "existing",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void getAllIsOkGetsAllIngredientsAsShortInfoDTOs() throws Exception {
//        Arrange
        Ingredient ingTest1 = new Ingredient()
                .setName("ingredientTest1")
                .setDescription("Lorem ipsum odor amet, consectetuer adipiscing elit. Donec ridiculus morbi ut pulvinar eleifend euismod libero etiam rhoncus conubia aenean.")
                .setUnit(UnitEnum.GRAMS)
                .setIngredientType(IngredientType.MUSHROOM)
                .setAddedBy(TEST_USER);

        Ingredient ingTest2 = new Ingredient()
                .setName("ingredientTest2")
                .setDescription("Lorem ipsum odor amet, consectetuer adipiscing elit. Donec ridiculus morbi ut pulvinar eleifend euismod libero etiam rhoncus conubia aenean.")
                .setUnit(UnitEnum.MILLILITRES)
                .setIngredientType(IngredientType.ALCOHOL)
                .setAddedBy(TEST_USER);

        List<Ingredient> entities = this.ingredientRepository.saveAll(List.of(ingTest1, ingTest2));

//        Act

        MvcResult mvcResult = mockMvc.perform(get("/ingredients/all"))
                .andExpect(status().isOk())
                .andReturn();

//        Assert

        ModelAndView actualMaV = mvcResult.getModelAndView();
        assertNotNull(actualMaV);
        assertFalse(actualMaV.isEmpty());
        assertEquals("ingredients-all", actualMaV.getViewName());
        Map<String, Object> model = actualMaV.getModel();
        assertTrue(model.containsKey("all"));

        List<IngredientShortInfoDTO> actualDTOs = (List<IngredientShortInfoDTO>) model.get("all");
        assertEquals(this.ingredientRepository.count(), actualDTOs.size());

        for (IngredientShortInfoDTO actualDTO : actualDTOs) {

            Optional<Ingredient> optIng = this.ingredientRepository.findById(actualDTO.getId());
            assertTrue(optIng.isPresent());
            Ingredient ing = optIng.get();

            assertEquals(ing.getName(), actualDTO.getName());
            assertEquals(ing.getDescription(), actualDTO.getDescription());
            assertEquals(ing.getIngredientType(), actualDTO.getType());
        }

    }
    
    @Test
    @WithAnonymousUser
    void testGetDetailsRedirects_ForAnonymous() throws Exception {
        Ingredient testIng = this.ingredientRepository.save(NEW_INGREDIENT.setAddedBy(TEST_USER));
        
        mockMvc.perform(get("/ingredients/" + testIng.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USER_LOGIN_URL));
    }

    @Test
    @WithUserDetails(
            value = "existing",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testGetDetailsIsOkAnd_ReturnsIngredientDetailsDTO() throws Exception {
        Ingredient ingEntity = this.ingredientRepository.save(NEW_INGREDIENT.setAddedBy(TEST_USER));

        MvcResult mvcResult = mockMvc.perform(get("/ingredients/" + ingEntity.getId()))
                .andExpect(status().isOk())
                .andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        assertNotNull(mav);
        assertTrue(mav.hasView());
        assertEquals("ingredient-details", mav.getViewName());
        assertTrue(mav.getModel().containsKey("ingredient"));
        Object actualAttrObj = mav.getModel().get("ingredient");
        assertInstanceOf(IngredientDetailsDTO.class, actualAttrObj);
        IngredientDetailsDTO actualAttrDTO = (IngredientDetailsDTO) actualAttrObj;

        assertEquals(ingEntity.getId(), actualAttrDTO.getId());
        assertEquals(ingEntity.getName(), actualAttrDTO.getName());
        assertEquals(ingEntity.getDescription(), actualAttrDTO.getDescription());
        assertEquals(ingEntity.getUnit(), actualAttrDTO.getUnit());
        assertEquals(ingEntity.getIngredientType(), actualAttrDTO.getType());
        assertEquals(ingEntity.getAddedBy().getUsername(), actualAttrDTO.getAddedByUsername());
    }
}
