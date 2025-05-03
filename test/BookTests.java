package test;

import main.Database;
import main.models.Book;
import main.models.Publisher;
import main.services.BookService;
import main.services.LibrarianService;
import main.services.PublisherService;
import main.services.UserService;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Book model
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookTests {
    static Database database;
    static UserService userService;
    static LibrarianService librarianService;
    static PublisherService publisherService;
    static BookService bookService;

    static List<Publisher> createdPublishers = new ArrayList<>();
    static List<Book> createdBooks = new ArrayList<>();

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
    }

    /**
     * Closes database connection and created models
     */
    @AfterAll
    static void cleanUpDatabase() {
        TestUtils.clearModels(bookService, createdBooks);
        TestUtils.clearModels(publisherService, createdPublishers);

        database.close();
    }

    static Book bookEntity;
    static Publisher publisherEntity;

    /**
     * Tests creating book in database
     */
    @Test
    @Order(1)
    void createBook() {
        Publisher publisher = new Publisher(0, "Test Publisher", "Austria", "9876543210");
        createdPublishers.add(publisher);

        assertDoesNotThrow(() -> {
            publisherService.persist(publisher);
        });
        publisherEntity = publisher;

        Book book = new Book(0, "Test Book", "Test Author", publisher, 2000, "0-8940-5970-X");
        createdBooks.add(book);

        assertDoesNotThrow(() -> {
            bookService.persist(book);
        });

        bookEntity = book;
    }

    /**
     * Tests getting book from database
     */
    @Test
    @Order(2)
    void getBook() {
        assertNotNull(bookEntity);
        assertNotNull(publisherEntity);

        Book book = bookService.getById(bookEntity.getId());
        assertNotNull(book);
        assertEquals("Test Book", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals(publisherEntity.getId(), book.getPublisher().getId());
        assertEquals("Test Publisher", book.getPublisher().getName());
        assertEquals(2000, book.getPublicationYear());
        assertEquals("0-8940-5970-X", book.getIsbn());
    }

    /**
     * Tests deleting associated publisher without deleting the book
     */
    @Test
    @Order(3)
    void deletePublisherBeforeBooks() {
        assertNotNull(publisherEntity);

        assertThrows(Exception.class, () -> {
            publisherService.remove(publisherEntity);
        });
    }

    /**
     * Tests deleting book from database
     */
    @Test
    @Order(4)
    void deleteBook() {
        assertNotNull(bookEntity);

        assertDoesNotThrow(() -> {
            bookService.remove(bookEntity);
        });
        createdBooks.remove(bookEntity);
    }
}