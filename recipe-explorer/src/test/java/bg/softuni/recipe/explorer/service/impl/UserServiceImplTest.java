package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.exceptions.ObjectNotFoundException;
import bg.softuni.recipe.explorer.exceptions.UserRegisterPasswordsConfirmationMismatch;
import bg.softuni.recipe.explorer.model.dto.UserRegisterDTO;
import bg.softuni.recipe.explorer.model.entity.Role;
import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.model.enums.RoleEnum;
import bg.softuni.recipe.explorer.repository.RoleRepository;
import bg.softuni.recipe.explorer.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//TODO: @Disabled
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private final static String TEST_USERNAME = "testUsername";
    private final static String TEST_EMAIL = "testuser@user.com";
    private final static Long TEST_ID = 1L;
    private final static Long TEST_INVALID_ID = 13L;
    private final static String TEST_VALID_PASSWORD = "test";

    private User testUser;
    private UserRegisterDTO testRegisterDTO;
    private Role testRole;

    private UserServiceImpl toTest;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private RoleRepository mockRoleRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;

// TODO: ModelMapper ?

    @BeforeEach
    void setUp() {
        toTest = new UserServiceImpl(mockUserRepository, mockRoleRepository, mockPasswordEncoder, new ModelMapper());

        testUser = new User()
                .setId(TEST_ID)
                .setUsername(TEST_USERNAME)
                .setEmail("testuser@user.com")
                .setFirstName("testUserFirstName")
                .setLastName("testUserov")
                .setRoles(Set.of(
                        new Role().setName(RoleEnum.USER))
                ).setPassword(TEST_VALID_PASSWORD);

        testRegisterDTO = new UserRegisterDTO()
                .setUsername(TEST_USERNAME)
                .setEmail("testuser@user.com")
                .setFirstName("testUserFirstName")
                .setLastName("testUserov")
                .setPassword(TEST_VALID_PASSWORD)
                .setConfirmPassword("test");

        testRole = new Role()
                .setName(RoleEnum.USER);
    }

    @Test
    void testIsUsernameUnique() {
//        TODO: more appropriate with IT

        when(mockUserRepository.existsByUsername(TEST_USERNAME))
                .thenReturn(true);

        assertFalse(toTest.isUsernameUnique(TEST_USERNAME));
    }

    @Test
    void testIsEmailUnique() {
        when(mockUserRepository.existsByEmail(TEST_EMAIL))
                .thenReturn(true);

        assertFalse(toTest.isEmailUnique(TEST_EMAIL));
    }

    @Test
    void testGetUserById_ThrowsObjectNotFound() {
        assertThrows(ObjectNotFoundException.class, () -> toTest.getUserById(TEST_INVALID_ID));
    }

    @Test
    void testDoRegister_ThrowsForPasswordsMismatch() {
        String invalidPassword = "invalid";

        testRegisterDTO.setPassword(null);
        assertThrows(
                UserRegisterPasswordsConfirmationMismatch.class,
                () -> toTest.doRegister(testRegisterDTO));

        testRegisterDTO.setPassword(invalidPassword);
        assertThrows(
                UserRegisterPasswordsConfirmationMismatch.class,
                () -> toTest.doRegister(testRegisterDTO));

        testRegisterDTO.setPassword(TEST_VALID_PASSWORD);
        testRegisterDTO.setConfirmPassword(null);
        assertThrows(
                UserRegisterPasswordsConfirmationMismatch.class,
                () -> toTest.doRegister(testRegisterDTO));

        testRegisterDTO.setConfirmPassword(invalidPassword);
        assertThrows(
                UserRegisterPasswordsConfirmationMismatch.class,
                () -> toTest.doRegister(testRegisterDTO));
    }

    @Test
    void testDoRegister() {

//        Arrange
        when(mockPasswordEncoder.encode(testRegisterDTO.getPassword()))
                .thenReturn(testRegisterDTO.getPassword());

        when(mockRoleRepository.findByName(RoleEnum.USER))
                .thenReturn(Optional.of(testRole));

//        Act
        toTest.doRegister(testRegisterDTO);

//        Assert
        verify(mockUserRepository).save(userCaptor.capture());
        User actualUser = userCaptor.getValue();

        assertNotNull(actualUser);
        assertEquals(testRegisterDTO.getUsername(), actualUser.getUsername());
        assertEquals(testRegisterDTO.getEmail(), actualUser.getEmail());
        assertEquals(testRegisterDTO.getFirstName(), actualUser.getFirstName());
        assertEquals(testRegisterDTO.getLastName(), actualUser.getLastName());
        assertEquals(testRegisterDTO.getPassword(), actualUser.getPassword());
    }
}
