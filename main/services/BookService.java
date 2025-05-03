package main.services;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import main.models.Book;
import main.models.Copy;
import org.hibernate.Session;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Book Service
 */
public class BookService extends BaseService<Book> {

    /**
     * Book Service constructor
     * @param session database session
     */
    public BookService(Session session) {
        super(Book.class, session);
    }

    /**
     * get available books
     * @return all available books
     */
    public List<Book> getAvailableBooks() {
        System.out.println("getAvailableBooks BookService.Java(services)");
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = criteriaQuery.from(Book.class);
        Join<Book, Copy> userJoin = root.join("copy");
        criteriaQuery.select(root).where(criteriaBuilder.equal(userJoin.get("status"), "Available"));
        return session.createQuery(criteriaQuery).list();
    }

    @Override
    public void persist(Book item) throws Exception {
        System.out.println("persist BookService.Java(services)");
        if(!isValidIsbn(item.getIsbn())) {
            throw new Exception("Invalid ISBN: " + item.getIsbn());
        }

        super.persist(item);
    }

    @Override
    public Book merge(Book item) throws Exception {
        System.out.println("merge BookService.Java(services)");
        if(!isValidIsbn(item.getIsbn())) {
            throw new Exception("Invalid ISBN: " + item.getIsbn());
        }

        return super.merge(item);
    }

    /**
     * checks isbn validity
     * @param isbn isbn to check
     * @return true if isbn is valid
     */
    public static boolean isValidIsbn(String isbn) {
        System.out.println("isValidIsbn");
        if(isbn == null || isbn.isEmpty()) {
            return false;
        }

        if (isbn.length() == 10 + 3 /* 3 dashes */) {
            return isValidIsbn10(isbn);
        }
        if (isbn.length() == 13 + 4 /* 4 dashes */) {
            return isValidIsbn13(isbn);
        }

        return false;
    }

    /**
     * checks isbn validity for isbn-10
     * @param isbn isbn to check
     * @return true if isbn is valid
     */
    static boolean isValidIsbn10(String isbn) {
        System.out.println("isValidIsbn10 BookService.Java(services)");
        // 0-2137-6653-1
        // 0-1998-1069-9
        // 0-8940-5970-X
        // 0-1432-1829-8

        for (int i : Arrays.asList(0, /* dash */ 2, 3, 4, 5, /* dash */ 7, 8, 9, 10 /* dash */)) {
            if(!Character.isDigit(isbn.charAt(i))) {
                return false;
            }
        }
        for (int i : Arrays.asList(1, 6, 11)) {
            if(isbn.charAt(i) != '-') {
                return false;
            }
        }
        if(isbn.charAt(12) != 'X' && !Character.isDigit(isbn.charAt(12))) {
            return false;
        }

        String numericIsbn = isbn.replace("-", "");
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(numericIsbn.charAt(i));
            sum += digit * (10 - i);
        }

        char lastChar = numericIsbn.charAt(9);
        int lastDigit;
        if (lastChar == 'X') {
            lastDigit = 10;
        } else {
            lastDigit = Character.getNumericValue(lastChar);
        }

        return (sum + lastDigit) % 11 == 0;
    }

    /**
     * checks isbn validity for isbn-13
     * @param isbn isbn to check
     * @return true if isbn is valid
     */
    static boolean isValidIsbn13(String isbn) {
        System.out.println("isValidIsbn13 BookService.Java(services)");
        // 978-1-9609-5434-3
        // 978-6-1185-7752-8
        // 978-3-4522-8634-5
        // 978-4-4198-9239-5

        for (int i : Arrays.asList(0, 1, 2, /* dash */ 4, /* dash */ 6, 7, 8, 9, /* dash */ 11, 12, 13, 14 /* dash */, 16)) {
            if(!Character.isDigit(isbn.charAt(i))) {
                return false;
            }
        }
        for (int i : Arrays.asList(3, 5, 10, 15)) {
            if(isbn.charAt(i) != '-') {
                return false;
            }
        }

        String numericIsbn = isbn.replace("-", "");
        int sum = 0;
        for (int i = 0; i < 13; i++) {
            int digit = Character.getNumericValue(numericIsbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        return sum % 10 == 0;
    }
}
