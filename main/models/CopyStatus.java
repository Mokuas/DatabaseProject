package main.models;

/**
 * Current status of book copies
 */
public interface CopyStatus {
    /**
     * Can be borrowed
     */
    public static final String AVAILABLE = "Available";

    /**
     * Already borrowed
     */
    public static final String BORROWED = "Borrowed";

    /**
     * Can not be borrowed anymore
     */
    public static final String WITHDRAWN = "Withdrawn";
}

