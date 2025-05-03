package main.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

/**
 * Copy model
 */
@Entity
@Table(name = "copies")
public class Copy {

    /**
     * id field
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * book field
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    /**
     * copy number field
     */
    @Column(nullable = false)
    private int copyNumber;

    /**
     * status field
     */
    @Column(nullable = false)
    @ColumnDefault("'" + CopyStatus.AVAILABLE + "'")
    private String status; // e.g. Available, Borrowed, Withdrawn

    /**
     * borrowings field for managing relation
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "copy_id", referencedColumnName = "id")
    private List<Borrowing> borrowing;

    /**
     *
     * @return string representation of copy
     */
    @Override
    public String toString() {
        System.out.println("toString Copy.java(mdoels)");
        return id + " - " + "Book " + book.getId() + ", Copy " + copyNumber + " > " + status;
    }

    /**
     * Default empty constructor
     */
    public Copy() {
        System.out.println("Copy1 Copy.java(mdoels)");
    }

    /**
     * Constructor with fields
     * @param id id
     * @param book book
     * @param copyNumber copy number
     * @param status status
     */
    public Copy(int id, Book book, int copyNumber, String status) {
        System.out.println("Copy2 Copy.java(mdoels)");
        this.id = id;
        this.book = book;
        this.copyNumber = copyNumber;
        this.status = status;
    }

    /**
     * getter for id field
     * @return id
     */
    public int getId() {
        System.out.println("getId Copy.java(mdoels)");
        return id;
    }

    /**
     * setter for id field
     * @param id id
     */
    public void setId(int id) {
        System.out.println("setId Copy.java(mdoels)");
        this.id = id;
    }

    /**
     * getter for book field
     * @return book
     */
    public Book getBook() {
        System.out.println("getBook Copy.java(mdoels)");
        return book;
    }

    /**
     * setter for book field
     * @param book book
     */
    public void setBook(Book book) {
        System.out.println("setBook Copy.java(mdoels)");
        this.book = book;
    }

    /**
     * getter for copy number field
     * @return copy number
     */
    public int getCopyNumber() {
        System.out.println("getCopyNumber Copy.java(mdoels)");
        return copyNumber;
    }

    /**
     * setter for copy number field
     * @param copyNumber copy number
     */
    public void setCopyNumber(int copyNumber) {
        System.out.println("setCopyNumber Copy.java(mdoels)");
        this.copyNumber = copyNumber;
    }

    /**
     * getter for status field
     * @return status
     */
    public String getStatus() {
        System.out.println("getStatus Copy.java(mdoels)");
        return status;
    }

    /**
     * setter for status field
     * @param status status
     */
    public void setStatus(String status) {
        System.out.println("setStatus Copy.java(mdoels)");
        this.status = status;
    }
}
