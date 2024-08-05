package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.model.enums.RoleEnum;
import bg.softuni.recipe.explorer.repository.RoleRepository;
import bg.softuni.recipe.explorer.repository.UserRepository;
import bg.softuni.recipe.explorer.service.init.RoleInitServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {


    private final static User USER_TEST =
            new User().setUsername("testUser")
                    .setFirstName("firstNameTest")
                    .setLastName("lastNameTest")
                    .setEmail("test@user.com")
                    .setPassword("password");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleInitServiceImpl roleInitService;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void init() {
//        TODO: Disable DbInit

        this.roleInitService.init();

        this.userRepository.save(
                new User()
                        .setUsername("existing")
                        .setEmail("existing@user.com")
                        .setFirstName("existingFirstName")
                        .setLastName("existingLastName")
                        .setPassword(passwordEncoder.encode("password"))
                        .setRoles(Set.of(this.roleRepository.findByName(RoleEnum.USER).orElseThrow())
                        )
        );
    }

    @AfterEach
    void tearDown() {
//        TODO: Disable DbInit

        this.userRepository.deleteAll();
    }

    @Test
    @WithUserDetails(
            value = "existing",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetLoginAndRegisterRedirects_ForLoggedUser() throws Exception {

        mockMvc.perform(get("/users/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        mockMvc.perform(get("/users/register"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    @WithAnonymousUser
    void testGetsLoginAndRegister_ForAnonymous() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testPostRegister() throws Exception {
        ResultActions result = mockMvc.perform(post("/users/register")
                        .param("username", USER_TEST.getUsername())
                        .param("email", USER_TEST.getEmail())
                        .param("firstName", USER_TEST.getFirstName())
                        .param("lastName", USER_TEST.getLastName())
                        .param("password", USER_TEST.getPassword())
                        .param("confirmPassword", USER_TEST.getPassword())
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));

        Optional<User> optUser = this.userRepository.findByUsername(USER_TEST.getUsername());

        Assertions.assertTrue(optUser.isPresent());

        User actual = optUser.get();

        Assertions.assertEquals(USER_TEST.getUsername(), actual.getUsername());
        Assertions.assertEquals(USER_TEST.getEmail(), actual.getEmail());
        Assertions.assertEquals(USER_TEST.getFirstName(), actual.getFirstName());
        Assertions.assertEquals(USER_TEST.getLastName(), actual.getLastName());
        Assertions.assertTrue(passwordEncoder.matches(USER_TEST.getPassword(), actual.getPassword()));

//        TODO: how to test roles
    }

    @Test
    void testPostRegister_ForInvalidData_Redirects() throws Exception {
//        TODO:

        mockMvc.perform(post("/users/register")
                        .param("username", "x")
                        .param("email", "x")
                        .param("firstName", "x")
                        .param("lastName", "x")
                        .param("password", "x")
                        .param("confirmPassword", "x")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"));
    }

    @Test
    void testPostRegister_ForPasswordMismatch_Redirects() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", USER_TEST.getUsername())
                        .param("email", USER_TEST.getEmail())
                        .param("firstName", USER_TEST.getFirstName())
                        .param("lastName", USER_TEST.getLastName())
                        .param("password", "xxxxx")
                        .param("confirmPassword", "yyyyy")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"));
    }
}
