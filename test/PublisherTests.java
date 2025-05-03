package test;

import main.Database;
import main.models.Librarian;
import main.models.Publisher;
import main.models.User;
import main.services.LibrarianService;
import main.services.PublisherService;
import main.services.UserService;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Publisher model
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PublisherTests {
    static Database database;
    static PublisherService publisherService;

    static List<Publisher> createdPublishers = new ArrayList<>();

    /**
     * Initializes database connection and necessary services
     */
    @BeforeAll
    static void initializeDatabase() {
        database = new Database();
        database.initialize();

        publisherService = new PublisherService(database.getSession());
    }

    /**
     * Closes database connection and created models
     */
    @AfterAll
    static void cleanUpDatabase() {
        TestUtils.clearModels(publisherService, createdPublishers);

        database.close();
    }

    static Publisher publisherEntity;

    /**
     * Tests creating publisher in database
     */
    @Test
    @Order(1)
    void createPublisher() {
        Publisher publisher = new Publisher(0, "Test Publisher", "Iceland", "+12561231");
        createdPublishers.add(publisher);

        assertDoesNotThrow(() -> {
            publisherService.persist(publisher);
        });
        assertNotEquals(0, publisher.getId());

        publisherEntity = publisher;
    }

    /**
     * Tests getting publisher from database
     */
    @Test
    @Order(2)
    void getPublisher() {
        assertNotNull(publisherEntity);
        assertNotEquals(0, publisherEntity.getId());

        Publisher publisher = publisherService.getById(publisherEntity.getId());
        assertNotNull(publisher);
        assertEquals(publisherEntity.getId(), publisher.getId());
        assertEquals("Test Publisher", publisher.getName());
        assertEquals("Iceland", publisher.getAddress());
        assertEquals("+12561231", publisher.getPhoneNumber());
    }

    /**
     * Tests updating fields of publisher in database
     */
    @Test
    @Order(3)
    void updatePublisher() {
        Publisher publisher = publisherService.getById(publisherEntity.getId());
        assertNotNull(publisher);

        publisher.setAddress("New Iceland");
        assertDoesNotThrow(() -> {
            publisherService.merge(publisher);
        });

        assertEquals("New Iceland", publisherEntity.getAddress());
    }

    /**
     * Tests deleting publisher from database
     */
    @Test
    @Order(4)
    void deletePublisher() {
        assertDoesNotThrow(() -> {
            publisherService.remove(publisherEntity);
        });
        createdPublishers.remove(publisherEntity);
    }
}
