package test;

import main.Database;
import main.models.User;
import main.services.UserService;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for User model
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserTests {
    static Database database;
    static UserService userService;
    static List<User> createdUsers = new ArrayList<>();

    /**
     * Initializes database connection and necessary services
     */
    @BeforeAll
    static void initializeDatabase() {
        database = new Database();
        database.initialize();

        userService = new UserService(database.getSession());
    }

    /**
     * Closes database connection and created models
     */
    @AfterAll
    static void cleanUpDatabase() {
        TestUtils.clearModels(userService, createdUsers);

        database.close();
    }

    static int userToUpdateAndDeleteId;

    /**
     * Tests creating user in database
     */
    @Test
    @Order(1)
    void createUser() {
        User user = new User(0, "First User", "user1@email.com", "5555555555", "Indonesia");
        createdUsers.add(user);

        assertDoesNotThrow(() -> {
            userService.persist(user);
        });
        userToUpdateAndDeleteId = user.getId();

        assertNotEquals(0, user.getId());
        assertEquals("First User", user.getName());
        assertEquals("user1@email.com", user.getEmail());
        assertEquals("5555555555", user.getPhoneNumber());
        assertEquals("Indonesia", user.getAddress());
    }

    /**
     * Tests getting user from database
     */
    @Test
    @Order(2)
    void getUser() {
        assertNotEquals(0, userToUpdateAndDeleteId);
        User user = userService.getById(userToUpdateAndDeleteId);
        assertNotNull(user);
        assertEquals(userToUpdateAndDeleteId, user.getId());
        assertEquals("First User", user.getName());
        assertEquals("user1@email.com", user.getEmail());
        assertEquals("5555555555", user.getPhoneNumber());
        assertEquals("Indonesia", user.getAddress());
    }

    /**
     * Tests updating fields of user in database
     */
    @Test
    @Order(3)
    void updateUser() {
        assertNotEquals(0, userToUpdateAndDeleteId);
        User userToUpdateAndDelete = userService.getById(userToUpdateAndDeleteId);
        assertNotNull(userToUpdateAndDelete);
        assertEquals("First User", userToUpdateAndDelete.getName());
        userToUpdateAndDelete.setName("New First User");
        userToUpdateAndDelete.setEmail("newuser1@email.com");
        userToUpdateAndDelete.setPhoneNumber("6666666666");
        userToUpdateAndDelete.setAddress("New Zealand");
        assertDoesNotThrow(() -> {
            userService.merge(userToUpdateAndDelete);
        });

        User user = userService.getById(userToUpdateAndDelete.getId());
        assertNotNull(user);
        assertEquals(userToUpdateAndDelete.getId(), user.getId());
        assertEquals("New First User", user.getName());
        assertEquals("newuser1@email.com", user.getEmail());
        assertEquals("6666666666", user.getPhoneNumber());
        assertEquals("New Zealand", user.getAddress());
    }

    /**
     * Tests deleting user from database
     */
    @Test
    @Order(4)
    void deleteUser() {
        assertNotEquals(0, userToUpdateAndDeleteId);
        User userToUpdateAndDelete = userService.getById(userToUpdateAndDeleteId);
        assertNotNull(userToUpdateAndDelete);

        assertDoesNotThrow(() -> {
            userService.remove(userToUpdateAndDelete);
        });

        createdUsers.remove(userToUpdateAndDelete); // user is already deleted. No need to delete again
    }


    /**
     * Tests creating multiple users with same email address
     */
    @Test
    void checkDuplicateEmails() {
        User user1 = new User(0, "Created User 1", "unique@email.com", "5555555555", "France");
        createdUsers.add(user1);

        User user2 = new User(0, "Created User 2", "unique@email.com", "5555555555", "Italy");
        createdUsers.add(user2);


        assertDoesNotThrow(() -> {
            userService.persist(user1);
        });

        assertThrows(Exception.class, () -> {
            userService.persist(user2);
        });
    }

    /**
     * Tests creating user with and invalid email address
     */
    @Test
    void checkInvalidEmail() {
        User user = new User(0, "Invalid Email User", "invalid-email-here", "5555555555", "Germany");
        assertThrows(Exception.class, () -> {
            userService.persist(user);
        });
    }
}
