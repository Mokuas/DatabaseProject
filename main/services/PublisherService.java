package main.services;

import main.models.Publisher;
import org.hibernate.Session;

/**
 * Publisher Service
 */
public class PublisherService extends BaseService<Publisher> {
    Session session;

    /**
     * Publisher Service constructor
     * @param session database session
     */
    public PublisherService(Session session) {
        super(Publisher.class, session);
        this.session = session;
    }
}
