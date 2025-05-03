package main.services;

import org.hibernate.Session;

import java.util.List;

/**
 * Base Service model, provides basic CRUD operations
 * @param <T> model type
 */
public class BaseService<T> {
    /**
     * Database session
     */
    Session session;

    /**
     * Model type that service operates on
     */

    Class<T> type;

    /**
     * BaseService constructor
     * @param type model type
     * @param session database session
     */
    public BaseService(Class<T> type, Session session) {
        System.out.println("BaseService.Java(services)");
        this.session = session;
        this.type = type;
    }

    /**
     * Persist item in db (save operation)
     * @param item item to persist
     * @throws Exception on error
     */
    public void persist(T item) throws Exception {
        System.out.println("persist BaseService.Java(services)");
        session.beginTransaction();

        try {
            session.persist(item);
        } finally {
            session.getTransaction().commit();
        }
    }

    /**
     * Merge item in db (update operation)
     * @param item item to merge
     * @return merged item
     * @throws Exception on error
     */
    public T merge(T item) throws Exception {
        System.out.println("merge BaseService.Java(services)");
        session.beginTransaction();

        try {
            return session.merge(item);
        } finally {
            session.getTransaction().commit();
        }
    }

    /**
     * Get item with id
     * @param id id of the item
     * @return item
     */
    public T getById(int id) {
        System.out.println("getById BaseService.Java(services)");
        return session.get(type, id);
    }

    /**
     * Get all items
     * @return all items
     */
    public List<T> getAll() {
        System.out.println("getAll BaseService.Java(services)");
        List<T> items;
        items = session.createQuery("from " + type.getName(), type).list();
        return items;
    }

    /**
     * Remove given in db (delete operation)
     * @param item item to remove
     */
    public void remove(T item) {
        System.out.println("remove BaseService.Java(services)");
        session.beginTransaction();

        try {
            session.remove(item);
        } finally {
            session.getTransaction().commit();
        }
    }
}
