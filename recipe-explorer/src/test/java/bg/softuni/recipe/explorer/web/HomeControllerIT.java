package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.repository.RoleRepository;
import bg.softuni.recipe.explorer.repository.UserRepository;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void init() {
//        TODO: Disable DbInit

        this.userRepository.save(
                new User()
                        .setUsername("existing")
                        .setEmail("existing@user.com")
                        .setFirstName("existingFirstName")
                        .setLastName("existingLastName")
                        .setPassword("password")
        );
    }

    @AfterEach
    void tearDown() {
//        TODO: Disable DbInit

        this.userRepository.deleteAll();
    }

    @Test
    void getAbout() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void getIndex_ForAnonymous() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "existing",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void getIndexRedirects_ForLoggedUser() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    @WithUserDetails(value = "existing",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void getHome() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk());
    }

}
