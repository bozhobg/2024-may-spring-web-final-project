package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.model.entity.Role;
import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.model.enums.RoleEnum;
import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import bg.softuni.recipe.explorer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppUserDetailsServiceImplTest {

    private final static String TEST_USERNAME = "testUser";
    private final static String INVALID_USERNAME = "invalidUser";



    private User testUser;

    private AppUserDetailsServiceImpl toTest;
    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    public void setUp() {
        toTest = new AppUserDetailsServiceImpl(mockUserRepository);

        testUser = new User().setUsername(TEST_USERNAME)
                .setEmail("testuser@user.com")
                .setFirstName("testUserFirstName")
                .setLastName("testUserov")
                .setRoles(Set.of(
                        new Role().setName(RoleEnum.USER))
                ).setPassword("test");
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
//        Arrange
        when(mockUserRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(testUser));

//        Act
        UserDetails userDetailsActual = toTest.loadUserByUsername(TEST_USERNAME);

//        Assert
        assertInstanceOf(AppUserDetails.class, userDetailsActual);

        AppUserDetails actualAppUserDetails = (AppUserDetails) userDetailsActual;
        assertEquals(testUser.getUsername(), actualAppUserDetails.getUsername());
        assertEquals(testUser.getPassword(), actualAppUserDetails.getPassword());
        assertEquals(testUser.getFirstName(), actualAppUserDetails.getFirstName());
        assertEquals(testUser.getLastName(), actualAppUserDetails.getLastName());

        List<String> expectedRolesAsString = testUser.getRoles().stream()
                .map(r -> "ROLE_" + r.getName())
                .toList();
        List<String> actualRolesAsString = actualAppUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertEquals(expectedRolesAsString, actualRolesAsString);
    }

//    TODO: test roles mapping

    @Test
    void testLoadUserByUsername_ThrowsIfNotFound() {
        when(mockUserRepository.findByUsername(INVALID_USERNAME)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> toTest.loadUserByUsername(INVALID_USERNAME));
    }
}
