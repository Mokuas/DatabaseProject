package main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.File;

/**
 * Class for managing database connection and sessions
 */
public class Database {
    static SessionFactory sessionFactory;
    static Session session;

    /**
     * Empty constructor
     */
    public Database() {
        System.out.println("Database Cunstructor (Database.Java)");
    }

    /**
     * Initializes database by loading hibernate.cfg.xml file
     */
    public void initialize() {
        System.out.println("initialize (Database.Java)");
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure(new File("hibernate.cfg.xml"))
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException(e);
        }

        session = sessionFactory.openSession();
    }

    /**
     * Closes database connection if its open
     */
    public void close() {
        System.out.println("close (Database.Java)");
        if(session != null && session.isOpen()) {
            session.close();
        }
        if(sessionFactory != null && sessionFactory.isOpen()) {
            sessionFactory.close();
        }
    }


    /**
     * session getter
     * @return session
     */
    public Session getSession() {
        System.out.println("getSession (Database.Java)");
        return session;
    }
}
