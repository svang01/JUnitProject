package org.example;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserService userService;
    private User existingUser;

    @Before
    public void setUp() {
        userService = new UserService();

        existingUser = Mockito.mock(User.class);
        when(existingUser.getUsername()).thenReturn("existingUser");
        when(existingUser.getPassword()).thenReturn("password");
        when(existingUser.getEmail()).thenReturn("existinguser@example.com");

        Map<String, User> userDatabase = new HashMap<>();
        userDatabase.put(existingUser.getUsername(), existingUser);
        userService.userDatabase = userDatabase;
    }

    @Test
    public void testRegisterUser_Positive() {
        User newUser = Mockito.mock(User.class);
        when(newUser.getUsername()).thenReturn("newUser");
        when(newUser.getPassword()).thenReturn("password");
        when(newUser.getEmail()).thenReturn("newuser@example.com");

        boolean result = userService.registerUser(newUser);

        assertTrue(result);
        assertTrue(userService.userDatabase.containsKey(newUser.getUsername()));
    }

    @Test
    public void testRegisterUser_Negative_UserAlreadyExists() {
        User existingUser = Mockito.mock(User.class);
        when(existingUser.getUsername()).thenReturn("existingUser");
        when(existingUser.getPassword()).thenReturn("password");
        when(existingUser.getEmail()).thenReturn("existinguser@example.com");

        boolean result = userService.registerUser(existingUser);

        assertFalse(result);
    }

    @Test
    public void testLoginUser_Positive() {
        User user = userService.loginUser("existingUser", "password");

        assertNotNull(user);
        assertEquals(existingUser, user);
    }

    @Test
    public void testLoginUser_Negative_UserNotFound() {
        User user = userService.loginUser("nonexistentUser", "password");

        assertNull(user);
    }

    @Test
    public void testLoginUser_Negative_WrongPassword() {
        User user = userService.loginUser("existingUser", "incorrectPassword");

        assertNull(user);
    }

    @Test
    public void testUpdateUserProfile_Positive() {
        User user = new User("userToUpdate", "password", "user@example.com");
        userService.userDatabase.put(user.getUsername(), user);

        String newUsername = "newUsername";
        String newPassword = "newPassword";
        String newEmail = "newemail@example.com";

        boolean result = userService.updateUserProfile(user, newUsername, newPassword, newEmail);

        assertTrue(result);
        assertEquals(newUsername, user.getUsername());
        assertEquals(newPassword, user.getPassword());
        assertEquals(newEmail, user.getEmail());
        assertTrue(userService.userDatabase.containsKey(newUsername));
        assertFalse(userService.userDatabase.containsKey(user));
    }

    @Test
    public void testUpdateUserProfile_Negative_NewUsernameTaken() {
        User user = Mockito.mock(User.class);
        when(user.getUsername()).thenReturn("userToUpdate");
        when(user.getPassword()).thenReturn("password");
        when(user.getEmail()).thenReturn("user@example.com");
        userService.userDatabase.put(user.getUsername(), user);

        String newUsername = "existingUser";
        String newPassword = "newPassword";
        String newEmail = "newemail@example.com";

        boolean result = userService.updateUserProfile(user, newUsername, newPassword, newEmail);

        assertFalse(result);
        assertEquals("userToUpdate", user.getUsername()); // Username remains unchanged
        assertNotEquals(newPassword, user.getPassword()); // Password should not be updated
        assertNotEquals(newEmail, user.getEmail()); // Email should not be updated
        assertTrue(userService.userDatabase.containsKey(user.getUsername()));
    }
    @Test
    public void testRegisterUser_Edge_EmptyUsername() {
        User emptyUsernameUser = Mockito.mock(User.class);
        when(emptyUsernameUser.getUsername()).thenReturn("");
        when(emptyUsernameUser.getPassword()).thenReturn("password");
        when(emptyUsernameUser.getEmail()).thenReturn("emptyuser@example.com");

        boolean result = userService.registerUser(emptyUsernameUser);

        assertTrue(result);
    }

    @Test
    public void testRegisterUser_Edge_EmptyPassword() {
        User emptyPasswordUser = Mockito.mock(User.class);
        when(emptyPasswordUser.getUsername()).thenReturn("emptyPasswordUser");
        when(emptyPasswordUser.getPassword()).thenReturn("");
        when(emptyPasswordUser.getEmail()).thenReturn("emptypassworduser@example.com");

        boolean result = userService.registerUser(emptyPasswordUser);

        assertTrue(result);
    }

    @Test
    public void testLoginUser_Edge_EmptyUsernameAndPassword() {
        User user = userService.loginUser("", "");

        assertNull(user);
    }

    @Test
    public void testUpdateUserProfile_Edge_NoChanges() {
        User user = new User("existingUser", "password", "existinguser@example.com");
        userService.userDatabase.put(user.getUsername(), user);

        boolean result = userService.updateUserProfile(user, "existingUser", "password", "existinguser@example.com");

        assertFalse(result);
        assertEquals("existingUser", user.getUsername()); // Username remains unchanged
        assertEquals("password", user.getPassword()); // Password remains unchanged
        assertEquals("existinguser@example.com", user.getEmail()); // Email remains unchanged
        assertTrue(userService.userDatabase.containsKey(user.getUsername()));
    }
}


