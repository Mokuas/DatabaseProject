package main.models;

import jakarta.persistence.*;

import java.sql.Date;

/**
 * Borrowing model
 */
@Entity
@Table(name = "borrowings")
public class Borrowing {

    /**
     * id field
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * user field
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * copy field
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "copy_id", nullable = false)
    private Copy copy;

    /**
     * borrow date field
     */
    @Column(nullable = false)
    private Date borrowDate;

    /**
     * return date field
     */
    @Column(nullable = true)
    private Date returnDate;

    /**
     * Default empty constructor
     */
    public Borrowing() {
        System.out.println("Borrowing1 .java(models)");
    }

    /**
     *
     * @return string representation of borrowing
     */
    @Override
    public String toString() {
        System.out.println("toString Borrowing.java(models)");
        return id + " - Copy: " + copy.getId() + "[" + borrowDate.toString() + "] " + (returnDate != null ? "(Returned)" : "");
    }

    /**
     * Constructor with fields
     * @param id id
     * @param user book
     * @param copy copy
     * @param borrowDate borrow date
     * @param returnDate return date
     */
    public Borrowing(int id, User user, Copy copy, Date borrowDate, Date returnDate) {
        System.out.println("Borrowing2 Borrowing.java(models)");
        this.id = id;
        this.user = user;
        this.copy = copy;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    /**
     * getter for id field
     * @return int id
     */
    public int getId() {
        System.out.println("getId Borrowing.java(models)");
        return id;
    }

    /**
     * setter for id field
     * @param id int id
     */
    public void setId(int id) {
        System.out.println("setId Borrowing.java(models)");
        this.id = id;
    }

    /**
     * getter for borrow date field
     * @return Date borrow date
     */
    public Date getBorrowDate() {
        System.out.println("getBorrowDate Borrowing.java(models)");
        return borrowDate;
    }

    /**
     * setter for borrow date field
     * @param borrowDate Date borrow date
     */
    public void setBorrowDate(Date borrowDate) {
        System.out.println("setBorrowDate Borrowing.java(models)");
        this.borrowDate = borrowDate;
    }

    /**
     * getter for return date field
     * @return Date return date
     */
    public Date getReturnDate() {
        System.out.println("getReturnDate Borrowing.java(models)");
        return returnDate;
    }

    /**
     * setter for return date field
     * @param returnDate Date return date
     */
    public void setReturnDate(Date returnDate) {
        System.out.println("setReturnDate Borrowing.java(models)");
        this.returnDate = returnDate;
    }

    /**
     * getter for user field
     * @return User user
     */
    public User getUser() {
        System.out.println("getUser Borrowing.java(models)");
        return user;
    }

    /**
     * setter for user field
     * @param user User user
     */
    public void setUser(User user) {
        System.out.println("setUser Borrowing.java(models)");
        this.user = user;
    }

    /**
     * getter for copy field
     * @return Copy copy
     */
    public Copy getCopy() {
        System.out.println("getCopy Borrowing.java(models)");
        return copy;
    }

    /**
     * setter for copy field
     * @param copy Copy copy
     */
    public void setCopy(Copy copy) {
        System.out.println("setCopy Borrowing.java(models)");
        this.copy = copy;
    }
}
