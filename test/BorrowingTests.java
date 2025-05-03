package test;

import main.Database;
import main.models.*;
import main.services.*;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Borrowing model
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BorrowingTests {
    static Database database;
    static PublisherService publisherService;
    static BookService bookService;
    static BorrowingService borrowingService;
    static UserService userService;
    static CopyService copyService;

    static List<Publisher> createdPublishers = new ArrayList<>();
    static List<Book> createdBooks = new ArrayList<>();
    static List<Borrowing> createdBorrowings = new ArrayList<>();
    static List<User> createdUsers = new ArrayList<>();
    static List<Copy> createdCopies = new ArrayList<>();

    /**
     * Initializes database connection and necessary services
     */
    @BeforeAll
    static void initializeDatabase() {
        database = new Database();
        database.initialize();

        publisherService = new PublisherService(database.getSession());
        bookService = new BookService(database.getSession());
        borrowingService = new BorrowingService(database.getSession());
        userService = new UserService(database.getSession());
        copyService = new CopyService(database.getSession());
    }

    /**
     * Closes database connection and created models
     */
    @AfterAll
    static void cleanUpDatabase() {
        TestUtils.clearModels(borrowingService, createdBorrowings);
        TestUtils.clearModels(copyService, createdCopies);
        TestUtils.clearModels(bookService, createdBooks);
        TestUtils.clearModels(publisherService, createdPublishers);
        TestUtils.clearModels(userService, createdUsers);

        database.close();
    }

    static Publisher publisherEntity;
    static Book bookEntity;
    static User userEntity;
    static Borrowing borrowingEntity;
    static Copy copyEntity;

    /**
     * Creates necessary entities used in other test methods
     */
    @Test
    @Order(1)
    void createNecessaryEntities() {
        publisherEntity = new Publisher(0, "Test Publisher", "Poland", "+18723");
        createdPublishers.add(publisherEntity);
        assertDoesNotThrow(() -> {
            publisherService.persist(publisherEntity);
        });

        bookEntity = new Book(0, "Test Book", "Michael Jordan", publisherEntity, 2000, "978-4-4198-9239-5");
        createdBooks.add(bookEntity);
        assertDoesNotThrow(() -> {
            bookService.persist(bookEntity);
        });

        copyEntity = new Copy(0, bookEntity, 12, CopyStatus.AVAILABLE);
        createdCopies.add(copyEntity);
        assertDoesNotThrow(() -> {
            copyService.persist(copyEntity);
        });

        userEntity = new User(0, "Test User", "borrower_user@email.com", "+9187934172", "Saudi Arabia");
        createdUsers.add(userEntity);
        assertDoesNotThrow(() -> {
            userService.persist(userEntity);
        });

        assertNotEquals(0, publisherEntity.getId());
        assertNotEquals(0, bookEntity.getId());
        assertNotEquals(0, copyEntity.getId());
        assertNotEquals(0, userEntity.getId());
    }

    /**
     * Tests creating borrowing in database
     */
    @Test
    @Order(2)
    void createBorrowing() {
        assertNotNull(userEntity);
        assertNotNull(copyEntity);

        borrowingEntity = new Borrowing(0, userEntity, copyEntity, Date.valueOf("2000-12-30"), null);
        createdBorrowings.add(borrowingEntity);
        assertDoesNotThrow(() -> {
            borrowingService.persist(borrowingEntity);
        });
        assertNotEquals(0, borrowingEntity.getId());
    }

    /**
     * Tests getting borrowing from database
     */
    @Test
    @Order(3)
    void getBorrowing() {
        assertNotNull(borrowingEntity);
        assertNotNull(userEntity);
        assertNotNull(copyEntity);
        assertNotNull(bookEntity);

        Borrowing borrowing = borrowingService.getById(borrowingEntity.getId());
        assertNotNull(borrowing);
        assertEquals(userEntity.getId(), borrowing.getUser().getId());
        assertEquals(copyEntity.getId(), borrowing.getCopy().getId());
        assertEquals(bookEntity.getId(), borrowing.getCopy().getBook().getId());
    }

    /**
     * Tests copy state of the copy after borrowing
     */
    @Test
    @Order(4)
    void checkCopyState() {
        assertNotNull(copyEntity);

        assertEquals(CopyStatus.BORROWED, copyEntity.getStatus());
    }

    /**
     * Tests borrowing an already borrowed copy
     */
    @Test
    @Order(5)
    void tryBorrowingBorrowedCopy() {
        assertNotNull(userEntity);
        assertNotNull(copyEntity);
        assertNotNull(borrowingEntity);

        Borrowing borrowing = new Borrowing(0, userEntity, copyEntity, Date.valueOf("2000-12-30"), null);
        assertThrows(Exception.class, () -> {
            borrowingService.persist(borrowing);
        });
    }

    /**
     * Tests returning a book
     */
    @Test
    @Order(6)
    void returnBook() {
        assertNotNull(borrowingEntity);
        assertNotNull(copyEntity);

        borrowingEntity.setReturnDate(Date.valueOf("2002-12-30"));
        assertDoesNotThrow(() -> {
            borrowingService.merge(borrowingEntity);
        });
        assertEquals(CopyStatus.AVAILABLE, copyEntity.getStatus());
    }

    /**
     * Tests borrowing a book
     */
    @Test
    @Order(7)
    void borrowBook() {
        assertNotNull(borrowingEntity);
        assertNotNull(copyEntity);

        borrowingEntity.setReturnDate(null);
        assertDoesNotThrow(() -> {
            borrowingService.merge(borrowingEntity);
        });
        assertEquals(CopyStatus.BORROWED, copyEntity.getStatus());
    }

    /**
     * Tries to borrow same copy in different threads at the same time
     */
    @Test
    @Order(8)
    void concurrencyTest() {
        assertNotNull(bookEntity);
        assertNotNull(userEntity);
        assertNotEquals(0, bookEntity.getId());

        final AtomicInteger successCount = new AtomicInteger(0);
        final AtomicInteger failCount = new AtomicInteger(0);

        Copy copy = new Copy(0, bookService.getById(bookEntity.getId()), 200, CopyStatus.AVAILABLE);
        createdCopies.add(copy);
        try {
            copyService.persist(copy);
        } catch (Exception ex) {
            assertNotEquals(0, copy.getId());
        }
        User user = userService.getById(userEntity.getId());

        int threadCount = 10;
        Borrowing[] borrowings = new Borrowing[threadCount];
        for(int i=0; i<threadCount; i++) {
            borrowings[i] = new Borrowing(0, user, copy, Date.valueOf("2000-12-30"), null);
        }

        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                int finalI = i;
                executorService.submit(() -> {
                    try {
                        borrowingService.persist(borrowings[finalI]);
                        successCount.incrementAndGet();
                        createdBorrowings.add(borrowings[finalI]);

                    } catch (Exception ex) {
                        failCount.incrementAndGet();
                    }
                });
            }

            executorService.shutdown();
        }

        assertEquals(1, successCount.get());
        assertEquals(threadCount - 1, failCount.get());
        assertEquals(CopyStatus.BORROWED, copy.getStatus());
    }

    /**
     * Tests deleting related entities to borrowing without deleting borrowing
     */
    @Test
    @Order(9)
    void deleteRelatedEntitiesBeforeBorrowing() {
        assertNotNull(userEntity);
        assertNotNull(publisherEntity);
        assertNotNull(bookEntity);
        assertNotNull(copyEntity);

        assertThrows(Exception.class, () -> {
            userService.remove(userEntity);
        });
        assertThrows(Exception.class, () -> {
            publisherService.remove(publisherEntity);
        });
        assertThrows(Exception.class, () -> {
            bookService.remove(bookEntity);
        });
        assertThrows(Exception.class, () -> {
            copyService.remove(copyEntity);
        });
    }

    /**
     * Tests deleting borrowing from database
     */
    @Test
    @Order(10)
    void deleteBorrowing() {
        assertDoesNotThrow(() -> {
            borrowingService.remove(borrowingEntity);
        });
        createdBorrowings.remove(borrowingEntity);

        assertEquals(CopyStatus.AVAILABLE, copyEntity.getStatus()); // deleting borrowing must free copy
    }
}
