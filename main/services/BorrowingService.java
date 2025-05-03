package main.services;

import main.models.Borrowing;
import main.models.Copy;
import main.models.CopyStatus;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Borrowing Service
 */
public class BorrowingService extends BaseService<Borrowing> {
    CopyService copyService;

    static int MAX_BORROW_COUNT = 2;

    /**
     * Borrowing Service constructor
     * @param session database session
     */
    public BorrowingService(Session session) {
        super(Borrowing.class, session);

        this.copyService = new CopyService(session);
    }

    /**
     * get borrowings of user
     * @param userId user id of borrowings to get
     * @return all borrowings of user
     */
    public List<Borrowing> getBorrowingsOfUser(int userId) {
        System.out.println("getBorrowingsOfUser BorrowingService.java(services)");
        Query<Borrowing> query = session.createQuery("FROM Borrowing b WHERE b.user.id = :userId", Borrowing.class);
        query.setParameter("userId", userId);
        return query.list();
    }

    /**
     * internal method for checking borrowing records with a filter
     * @param copyId copy id of borrowings
     * @param borrowingId id to filter
     * @return if has any other borrowing record
     */
    boolean hasAnyBorrowingExcept(int copyId, int borrowingId) {
        System.out.println("hasAnyBorrowingExcept BorrowingService.java(services)");
        Query<Borrowing> query = session.createQuery("FROM Borrowing b WHERE b.copy.id = :copyId AND b.id != :borrowingId AND b.returnDate IS NULL", Borrowing.class);
        query.setParameter("copyId", copyId);
        query.setParameter("borrowingId", borrowingId);
        return !query.list().isEmpty();
    }

    /**
     * returns borrow count of a user with a filter
     * @param userId id of the user
     * @param borrowingId borrowing id to filter out
     * @return borrow count of a user
     */
    int getBorrowingCountOfUserExcept(int userId, int borrowingId) {
        System.out.println("getBorrowingCountOfUserExcept BorrowingService.java(services)");
        Query<Borrowing> query = session.createQuery("FROM Borrowing b WHERE b.user.id = :userId AND b.id != :borrowingId AND b.returnDate IS NULL", Borrowing.class);
        query.setParameter("userId", userId);
        query.setParameter("borrowingId", borrowingId);
        return query.list().size();
    }

    /**
     * Object used to lock asynchronous borrowing
     */
    private final static Object lock = new Object();

    @Override
    public void persist(Borrowing item) throws Exception {
        System.out.println("persist2 BorrowingService.java(services)");
        synchronized (lock)
        {
            if(item.getReturnDate() == null) {
                if(hasAnyBorrowingExcept(item.getCopy().getId(), item.getId())) {
                    throw new Exception("Copy is already borrowed");
                }
            } else {
                if(item.getReturnDate().before(item.getBorrowDate())) {
                    throw new Exception("Return date can not be before borrow date");
                }
            }

            if(item.getReturnDate() == null) {
                Copy copy = copyService.getById(item.getCopy().getId());
                if(!copy.getStatus().equals(CopyStatus.AVAILABLE)) {
                    throw new Exception("Copy is not borrowable. Status: " + copy.getStatus());
                }
                if(getBorrowingCountOfUserExcept(item.getUser().getId(), item.getId()) >= MAX_BORROW_COUNT) {
                    throw new Exception("Maximum borrow count exceeded. Can not borrow more");
                }

                copy.setStatus(CopyStatus.BORROWED);
            }


            super.persist(item);
        }
    }

    @Override
    public Borrowing merge(Borrowing item) throws Exception {
        System.out.println("merge BorrowingService.java(services)");
        synchronized (lock)
        {
            if(item.getReturnDate() == null) {
                if(hasAnyBorrowingExcept(item.getCopy().getId(), item.getId())) {
                    throw new Exception("Copy is already borrowed");
                }
                if(getBorrowingCountOfUserExcept(item.getUser().getId(), item.getId()) >= MAX_BORROW_COUNT) {
                    throw new Exception("Maximum borrow count exceeded. Can not borrow more");
                }

                item.getCopy().setStatus(CopyStatus.BORROWED);
            } else {
                if(item.getReturnDate().before(item.getBorrowDate())) {
                    throw new Exception("Return date can not be before borrow date");
                }

                if(!hasAnyBorrowingExcept(item.getCopy().getId(), item.getId())) {
                    item.getCopy().setStatus(CopyStatus.AVAILABLE);
                }
            }

            return super.merge(item);
        }
    }

    @Override
    public void remove(Borrowing item) {
        System.out.println("remove BorrowingService.java(services)");
        if(!hasAnyBorrowingExcept(item.getCopy().getId(), item.getId())) {
            item.getCopy().setStatus(CopyStatus.AVAILABLE);
        }
        super.remove(item);
    }
}
