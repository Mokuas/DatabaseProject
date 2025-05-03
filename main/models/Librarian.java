package main.models;

import jakarta.persistence.*;

import java.sql.Date;

/**
 * Librarian model
 */
@Entity
@Table(name = "librarians")
public class Librarian {

    /**
     * id field
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * user field
     */
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * employment date field
     */
    @Column(nullable = false)
    private Date employmentDate;

    /**
     * position field
     */
    @Column(nullable = false)
    private String position;

    /**
     * Default empty constructor
     */
    public Librarian() {
        System.out.println("Librarian1 Librarian.Java(models)");
    }

    /**
     * Constructor with fields
     * @param id id
     * @param user user
     * @param employmentDate employment date
     * @param position position
     */
    public Librarian(int id, User user, Date employmentDate, String position) {
        System.out.println("Librarian2 Librarian.Java(models)");
        this.id = id;
        this.user = user;
        this.employmentDate = employmentDate;
        this.position = position;
    }

    /**
     *
     * @return string representation of librarian
     */
    @Override
    public String toString() {
        System.out.println("toString Librarian.Java(models)");
        return getId() + " - " + user.getName() + "(" + user.getId() + ")";
    }

    /**
     * getter for id
     * @return id
     */
    public int getId() {
        System.out.println("getId Librarian.Java(models)");
        return id;
    }

    /**
     * setter for id
     * @param id id
     */
    public void setId(int id) {
        System.out.println("setId Librarian.Java(models)");
        this.id = id;
    }

    /**
     * getter for user field
     * @return user
     */
    public User getUser() {
        System.out.println("getUser Librarian.Java(models)");
        return user;
    }

    /**
     * setter for user field
     * @param user user
     */
    public void setUser(User user) {
        System.out.println("setUser Librarian.Java(models)");
        this.user = user;
    }

    /**
     * getter for employment date
     * @return employment date
     */
    public Date getEmploymentDate() {
        System.out.println("getEmploymentDate Librarian.Java(models)");
        return employmentDate;
    }

    /**
     * setter for employment date
     * @param employmentDate employment date
     */
    public void setEmploymentDate(Date employmentDate) {
        System.out.println("setEmploymentDate Librarian.Java(models)");
        this.employmentDate = employmentDate;
    }

    /**
     * getter for position
     * @return position
     */
    public String getPosition() {
        System.out.println("getPosition Librarian.Java(models)");
        return position;
    }

    /**
     * setter for position
     * @param position position
     */
    public void setPosition(String position) {
        System.out.println("setPosition Librarian.Java(models)");
        this.position = position;
    }
}
