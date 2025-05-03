package main.services;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import main.models.Copy;
import main.models.CopyStatus;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Copy Service
 */
public class CopyService extends BaseService<Copy> {

    /**
     * Copy Service constructor
     * @param session database session
     */
    public CopyService(Session session) {
        super(Copy.class, session);
    }

    @Override
    public void persist(Copy item) throws Exception {
        System.out.println("persist CopyService.java(services)");
        if(
            !item.getStatus().equals(CopyStatus.AVAILABLE) &&
            !item.getStatus().equals(CopyStatus.BORROWED) &&
            !item.getStatus().equals(CopyStatus.WITHDRAWN)
        ) {
            throw new Exception("Invalid Copy Status. Possible values: Available, Borrowed, Withdrawn");
        }

        super.persist(item);
    }

    @Override
    public Copy merge(Copy item) throws Exception {
        System.out.println("persist CopyService.java(services)");
        if(
            !item.getStatus().equals(CopyStatus.AVAILABLE) &&
            !item.getStatus().equals(CopyStatus.BORROWED) &&
            !item.getStatus().equals(CopyStatus.WITHDRAWN)
        ) {
            throw new Exception("Invalid Copy Status. Possible values: Available, Borrowed, Withdrawn");
        }

        return super.merge(item);
    }
}
