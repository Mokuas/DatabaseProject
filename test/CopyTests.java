package test;

import main.Database;
import main.models.Book;
import main.models.Copy;
import main.models.CopyStatus;
import main.models.Publisher;
import main.services.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Copy model
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CopyTests {
    static Database database;
    static UserService userService;
    static LibrarianService librarianService;
    static PublisherService publisherService;
    static BookService bookService;
    static CopyService copyService;

    static List<Publisher> createdPublishers = new ArrayList<>();
    static List<Book> createdBooks = new ArrayList<>();
    static List<Copy> createdCopies = new ArrayList<>();

    /**
     * Initializes database connection and necessary services
     */
    @BeforeAll
    static void initializeDatabase() {
        database = new Database();
        database.initialize();

        userService = new UserService(database.getSession());
        librarianService = new LibrarianService(database.getSession());
        publisherService = new PublisherService(database.getSession());
        bookService = new BookService(database.getSession());
        copyService = new CopyService(database.getSession());
    }

    /**
     * Closes database connection and created models
     */
    @AfterAll
    static void cleanUpDatabase() {
        TestUtils.clearModels(bookService, createdBooks);
        TestUtils.clearModels(publisherService, createdPublishers);
        TestUtils.clearModels(copyService, createdCopies);

        database.close();
    }

    static Book bookEntity;
    static Publisher publisherEntity;
    static Copy copyEntity;

    /**
     * Tests creating copy in database
     */
    @Test
    @Order(1)
    void createCopy() {
        Publisher publisher = new Publisher(0, "Test Publisher", "Austria", "9876543210");
        createdPublishers.add(publisher);

        assertDoesNotThrow(() -> {
            publisherService.persist(publisher);
        });
        publisherEntity = publisher;

        Book book = new Book(0, "Test Book", "Test Author", publisher, 2000, "978-4-4198-9239-5");
        createdBooks.add(book);

        assertDoesNotThrow(() -> {
            bookService.persist(book);
        });
        bookEntity = book;

        Copy copy = new Copy(0, book, 12, CopyStatus.AVAILABLE);
        createdCopies.add(copy);
        assertDoesNotThrow(() -> {
            copyService.persist(copy);
        });
        copyEntity = copy;
    }

    /**
     * Tests getting copy from database
     */
    @Test
    @Order(2)
    void getCopy() {
        assertNotNull(bookEntity);
        assertNotNull(publisherEntity);
        assertNotNull(copyEntity);

        Copy copy = copyService.getById(copyEntity.getId());
        assertNotNull(copy);
        assertEquals(copyEntity.getId(), copy.getId());
        assertEquals(bookEntity.getId(), copy.getBook().getId());
        assertEquals(12, copy.getCopyNumber());
        assertEquals(CopyStatus.AVAILABLE, copy.getStatus());
    }

    /**
     * Tests deleting book before the copy from database
     */
    @Test
    @Order(3)
    void deleteBookBeforeCopy() {
        assertNotNull(bookEntity);

        assertThrows(Exception.class, () -> {
            bookService.remove(bookEntity);
        });
    }

    /**
     * Tests deleting publisher before the copy from database
     */
    @Test
    @Order(4)
    void deletePublisherBeforeCopy() {
        assertNotNull(publisherEntity);

        assertThrows(Exception.class, () -> {
            publisherService.remove(publisherEntity);
        });
    }

    /**
     * Tests deleting copy from database
     */
    @Test
    @Order(5)
    void deleteCopy() {
        assertNotNull(copyEntity);

        assertDoesNotThrow(() -> {
            copyService.remove(copyEntity);
        });
        createdCopies.remove(copyEntity);
    }
}
