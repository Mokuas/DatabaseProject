package test;

import main.Database;
import main.LibrarianGui;
import main.models.Librarian;
import main.models.User;
import main.services.LibrarianService;
import main.services.UserService;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Librarian model
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LibrarianTests {
    static Database database;
    static UserService userService;
    static LibrarianService librarianService;

    static List<User> createdUsers = new ArrayList<>();
    static List<Librarian> createdLibrarians = new ArrayList<>();

    /**
     * Initializes database connection and necessary services
     */
    @BeforeAll
    static void initializeDatabase() {
        database = new Database();
        database.initialize();

        userService = new UserService(database.getSession());
        librarianService = new LibrarianService(database.getSession());
    }

    /**
     * Closes database connection and created models
     */
    @AfterAll
    static void cleanUpDatabase() {
        TestUtils.clearModels(librarianService, createdLibrarians);
        TestUtils.clearModels(userService, createdUsers);

        database.close();
    }

    static User userEntity;
    static Librarian librarianEntity;

    /**
     * Tests creating librarian in database
     */
    @Test
    @Order(1)
    void createLibrarian() {
        userEntity = new User(0, "Manager Person", "manager@email.com", "1234567890", "Austria");
        createdUsers.add(userEntity);
        assertDoesNotThrow(() -> {
            userService.persist(userEntity);
        });
        assertNotEquals(0, userEntity.getId());


        librarianEntity = new Librarian(0, userEntity, Date.valueOf("2000-12-30"), "Manager");
        createdLibrarians.add(librarianEntity);
        assertDoesNotThrow(() -> {
            librarianService.persist(librarianEntity);
        });
        assertNotEquals(0, librarianEntity.getId());
    }

    /**
     * Tests getting librarian from database
     */
    @Test
    @Order(2)
    void getLibrarian() {
        assertNotNull(librarianEntity);
        assertNotEquals(0, librarianEntity.getId());
        Librarian librarian = librarianService.getById(librarianEntity.getId());
        assertNotNull(librarian);
        assertEquals(librarianEntity.getId(), librarian.getId());

        assertEquals(userEntity.getId(), librarian.getUser().getId());
        assertEquals(Date.valueOf("2000-12-30"), librarian.getEmploymentDate());
        assertEquals("Manager", librarian.getPosition());
    }

    /**
     * Tests deleting user before librarian from database
     */
    @Test
    @Order(3)
    void deleteUserBeforeLibrarian() {
        assertNotNull(userEntity);
        assertNotEquals(0, userEntity.getId());
        User user = userService.getById(userEntity.getId());
        assertNotNull(user);

        assertNotNull(librarianEntity);
        assertNotEquals(0, librarianEntity.getId());
        Librarian librarian = librarianService.getById(librarianEntity.getId());
        assertNotNull(librarian);

        assertThrows(Exception.class, () -> {
            userService.remove(user);
        });
    }

    /**
     * Tests deleting librarian from database
     */
    @Test
    @Order(4)
    void deleteLibrarian() {
        assertNotNull(userEntity);
        assertNotEquals(0, userEntity.getId());
        User user = userService.getById(userEntity.getId());
        assertNotNull(user);

        assertNotNull(librarianEntity);
        assertNotEquals(0, librarianEntity.getId());
        Librarian librarian = librarianService.getById(librarianEntity.getId());
        assertNotNull(librarian);

        assertDoesNotThrow(() -> {
            librarianService.remove(librarian);

            // deleting a librarian should not delete the user, because Librarian.User does not have CascadeType.REMOVE
            assertNotNull(userService.getById(user.getId()));

            userService.remove(user);
        });
        createdLibrarians.remove(librarianEntity);
        createdUsers.remove(userEntity);
    }
}
