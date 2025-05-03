package main.models;

import jakarta.persistence.*;

import java.util.List;

/**
 * Book model
 */
@Entity
@Table(name = "books")
public class Book {

    /**
     * id field
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * title field
     */
    @Column(nullable = false)
    private String title;

    /**
     * author field
     */
    @Column(nullable = false)
    private String author;

    /**
     * publisher field
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;

    /**
     * publication year field
     */
    @Column(nullable = false)
    private int publicationYear;

    /**
     * isbn field
     */
    @Column(unique = true, nullable = false)
    private String isbn;

    /**
     * copies field for managing relation
     */
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private List<Copy> copy;

    /**
     * Default empty constructor
     */
    public Book() {
        System.out.println("Book1 (Book.Java(models))");
    }

    /**
     *
     * @return string representation of book
     */
    @Override
    public String toString() {
        System.out.println("toString (Book.Java(models)");
        return id + " - " + title;
    }

    /**
     * Constructor with fields
     * @param id id
     * @param title title
     * @param author author
     * @param publisher publisher
     * @param publicationYear publication year
     * @param isbn isbn
     */
    public Book(int id, String title, String author, Publisher publisher, int publicationYear, String isbn) {
        System.out.println("Book2 (Book.Java(models)");
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
    }

    /**
     * getter for id field
     * @return int id
     */
    public int getId() {
        System.out.println("getId (Book.Java(models)");
        return id;
    }

    /**
     * setter for id field
     * @param id int id
     */
    public void setId(int id) {
        System.out.println("setId (Book.Java(models)");
        this.id = id;
    }

    /**
     * getter for title field
     * @return String title
     */
    public String getTitle() {
        System.out.println("getTitle (Book.Java(models)");
        return title;
    }

    /**
     * setter for title field
     * @param title String title
     */
    public void setTitle(String title) {
        System.out.println("setTitle (Book.Java(models)");
        this.title = title;
    }

    /**
     * getter for author field
     * @return String author
     */
    public String getAuthor() {
        System.out.println("getAuthor (Book.Java(models)");
        return author;
    }

    /**
     * setter for author field
     * @param author String author
     */
    public void setAuthor(String author) {
        System.out.println("setAuthor (Book.Java(models)");
        this.author = author;
    }

    /**
     * getter for publication year field
     * @return int publication year
     */
    public int getPublicationYear() {
        System.out.println("getPublicationYear (Book.Java(models)");
        return publicationYear;
    }

    /**
     * setter for publication year field
     * @param publicationYear int publication year
     */
    public void setPublicationYear(int publicationYear) {
        System.out.println("setPublicationYear (Book.Java(models)");
        this.publicationYear = publicationYear;
    }

    /**
     * getter for isbn field
     * @return String isbn
     */
    public String getIsbn() {
        System.out.println("getIsbn (Book.Java(models)");
        return isbn;
    }

    /**
     * setter for isbn field
     * @param isbn String isbn
     */
    public void setIsbn(String isbn) {
        System.out.println("setIsbn (Book.Java(models)");
        this.isbn = isbn;
    }

    /**
     * getter for publisher field
     * @return Publisher publisher
     */
    public Publisher getPublisher() {
        System.out.println("getPublisher (Book.Java(models)");
        return publisher;
    }

    /**
     * setter for publisher field
     * @param publisher Publisher publisher
     */
    public void setPublisher(Publisher publisher) {
        System.out.println("setPublisher (Book.Java(models)");
        this.publisher = publisher;
    }
}
