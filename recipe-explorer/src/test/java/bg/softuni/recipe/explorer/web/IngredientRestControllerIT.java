package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.dto.IngredientBasicDTO;
import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.model.enums.IngredientType;
import bg.softuni.recipe.explorer.model.enums.RoleEnum;
import bg.softuni.recipe.explorer.model.enums.UnitEnum;
import bg.softuni.recipe.explorer.repository.IngredientRepository;
import bg.softuni.recipe.explorer.repository.RoleRepository;
import bg.softuni.recipe.explorer.repository.UserRepository;
import bg.softuni.recipe.explorer.service.init.RoleInitServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IngredientRestControllerIT {

    private final static String BASE_URL = "/api/ingredients";

    private User TEST_USER;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleInitServiceImpl roleInitService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

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

        List<Ingredient> ingredients = List.of(
                new Ingredient()
                        .setAddedBy(TEST_USER)
                        .setName("test1")
                        .setIngredientType(IngredientType.BEVERAGE)
                        .setUnit(UnitEnum.MILLILITRES)
                        .setDescription("Lorem ipsum odor amet, consectetuer adipiscing elit. Donec ridiculus morbi ut pulvinar eleifend euismod libero etiam rhoncus conubia aenean."
                        ),
                new Ingredient()
                        .setAddedBy(TEST_USER)
                        .setName("test2")
                        .setIngredientType(IngredientType.POULTRY)
                        .setUnit(UnitEnum.GRAMS)
                        .setDescription("Lorem ipsum odor amet, consectetuer adipiscing elit. Donec ridiculus morbi ut pulvinar eleifend euismod libero etiam rhoncus conubia aenean."
                        )
        );

        this.ingredientRepository.saveAll(ingredients);
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

        this.ingredientRepository.deleteAllById(ingIds);
        this.userRepository.deleteAllById(userIds);
    }

    @Test
    @WithUserDetails(
            value = "existing",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void getIngredientsAsBasicDTOs() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get(BASE_URL + "/short"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        List<IngredientBasicDTO> resultBasicDTOs = jacksonObjectMapper.readValue(json, new TypeReference<>() {});

        for (IngredientBasicDTO actualBasicResult : resultBasicDTOs) {
            Optional<Ingredient> optById = this.ingredientRepository.findById(actualBasicResult.getId());

            assertTrue(optById.isPresent());
            Ingredient expected = optById.get();
            assertEquals(expected.getId(), actualBasicResult.getId());
            assertEquals(expected.getName(), actualBasicResult.getName());
        }
    }
}
