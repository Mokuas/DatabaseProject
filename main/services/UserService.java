package main.services;

import main.models.User;
import org.hibernate.Session;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User Service
 */
public class UserService extends BaseService<User> {
    Session session;
    LibrarianService librarianService;

    /**
     * User Service constructor
     * @param session database session
     */
    public UserService(Session session) {
        super(User.class, session);
        this.session = session;
        this.librarianService = new LibrarianService(session);
    }

    @Override
    public void persist(User item) throws Exception {
        System.out.println("persist (UserService.java)");
        if(!isValidEmail(item.getEmail())) {
            throw new Exception("Invalid email");
        }
        super.persist(item);
    }

    @Override
    public User merge(User item) throws Exception {
        System.out.println("merge (UserService.java)");
        if(!isValidEmail(item.getEmail())) {
            throw new Exception("Invalid email");
        }
        System.out.println(item);
        return super.merge(item);
    }

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);

    /**
     * Checks email validity
     * @param email email address
     * @return true if email is valid
     */
    static boolean isValidEmail(String email) {
        System.out.println("isValidEmail (UserService.java)");
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }
}
