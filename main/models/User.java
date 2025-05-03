package main.models;

import jakarta.persistence.*;

/**
 * User model
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * id field
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * name field
     */
    @Column(nullable = false)
    private String name;

    /**
     * email field
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * phone number field
     */
    @Column(nullable = false)
    private String phoneNumber;

    /**
     * address field
     */
    @Column(nullable = false)
    private String address;

    /**
     * Default empty constructor
     */
    public User() {
        System.out.println("User1 User.java(models)");
    }

    /**
     *
     * @return string representation of User
     */
    @Override
    public String toString() {
        System.out.println("toString User.java(models)");
        return getId() + " - " + getName();
    }

    /**
     * Constructor with fields
     * @param id id
     * @param name name
     * @param email email
     * @param phoneNumber phone number
     * @param address address
     */
    public User(int id, String name, String email, String phoneNumber, String address) {
        System.out.println("User2 User.java(models)");
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    /**
     * getter for id field
     * @return id
     */
    public int getId() {
        System.out.println("getId User.java(models)");
        return id;
    }

    /**
     * setter for id field
     * @param id id
     */
    public void setId(int id) {
        System.out.println("setId User.java(models)");
        this.id = id;
    }

    /**
     * getter for name field
     * @return name
     */
    public String getName() {
        System.out.println("getName User.java(models)");
        return name;
    }

    /**
     * setter for name field
     * @param name name
     */
    public void setName(String name) {
        System.out.println("setName User.java(models)");
        this.name = name;
    }

    /**
     * getter for email field
     * @return email
     */
    public String getEmail() {
        System.out.println("getEmail User.java(models)");
        return email;
    }

    /**
     * setter for email field
     * @param email email
     */
    public void setEmail(String email) {
        System.out.println("setEmail User.java(models)");
        this.email = email;
    }

    /**
     * getter for phone number field
     * @return phone number
     */
    public String getPhoneNumber() {
        System.out.println("getPhoneNumber User.java(models)");
        return phoneNumber;
    }

    /**
     * setter for phone number field
     * @param phoneNumber phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        System.out.println("setPhoneNumber User.java(models)");
        this.phoneNumber = phoneNumber;
    }

    /**
     * getter for address field
     * @return address
     */
    public String getAddress() {
        System.out.println("getAddress User.java(models)");
        return address;
    }

    /**
     * setter for address field
     * @param address address
     */
    public void setAddress(String address) {
        System.out.println("setAddress User.java(models)");
        this.address = address;
    }
}
