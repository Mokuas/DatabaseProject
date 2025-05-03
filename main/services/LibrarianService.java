package main.services;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import main.models.Librarian;
import main.models.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.Date;
import java.util.List;

/**
 * Librarian Service
 */
public class LibrarianService extends BaseService<Librarian> {
    Session session;

    /**
     * Librarian Service constructor
     * @param session database session
     */
    public LibrarianService(Session session) {
        super(Librarian.class, session);
        this.session = session;
    }

    /**
     *
     * @return all librarians
     */
    public List<Librarian> getAll() {
        System.out.println("getAll LibrarianService.java(services)");
        return session.createQuery("from Librarian", Librarian.class).list();
    }

    /**
     * get librarian by user id
     * @param userId userId of the librarian to get
     * @return Librarian with given user id
     */
    public Librarian getLibrarianByUserId(int userId) {
        System.out.println("getLibrarianByUserId LibrarianService.java(services)");
        Query<Librarian> query = session.createQuery("FROM Librarian l JOIN l.user u WHERE u.id = :userId", Librarian.class);
        query.setParameter("userId", userId);
        return query.uniqueResult();

        /*
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Librarian> criteriaQuery = criteriaBuilder.createQuery(Librarian.class);
        Root<Librarian> root = criteriaQuery.from(Librarian.class);
        Join<Librarian, User> userJoin = root.join("user");
        criteriaQuery.select(root).where(criteriaBuilder.equal(userJoin.get("id"), userId));
        Query<Librarian> query = session.createQuery(criteriaQuery);
        return query.uniqueResult();
        */
    }
}
