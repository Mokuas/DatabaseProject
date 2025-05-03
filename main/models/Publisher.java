package main.models;

import jakarta.persistence.*;

import java.util.List;

/**
 * Publisher model
 */
@Entity
@Table(name = "publishers")
public class Publisher {

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
     * address field
     */
    @Column(nullable = false)
    private String address;

    /**
     * phone number field
     */
    @Column(nullable = false)
    private String phoneNumber;

    /**
     *
     * @return string representation of publisher
     */
    @Override
    public String toString() {
        System.out.println("toString Publisher.java(models)");
        return id + " - " + name;
    }

    /**
     * Default empty constructor
     */
    public Publisher() {
        System.out.println("Publisher1 Publisher.java(models)");
    }

    /**
     * Constructor with fields
     * @param id id
     * @param name name
     * @param address address
     * @param phoneNumber phone number
     */
    public Publisher(int id, String name, String address, String phoneNumber) {
        System.out.println("Publisher2 Publisher.java(models)");
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    /**
     * getter for id field
     * @return id
     */
    public int getId() {
        System.out.println("getId Publisher.java(models)");
        return id;
    }

    /**
     * setter for id field
     * @param id id
     */
    public void setId(int id) {
        System.out.println("setId Publisher.java(models)");
        this.id = id;
    }

    /**
     * getter for name field
     * @return name
     */
    public String getName() {
        System.out.println("getName Publisher.java(models)");
        return name;
    }

    /**
     * setter for name field
     * @param name name
     */
    public void setName(String name) {
        System.out.println("setName Publisher.java(models)");
        this.name = name;
    }

    /**
     * getter for address field
     * @return address
     */
    public String getAddress() {
        System.out.println("getAddress Publisher.java(models)");
        return address;
    }

    /**
     * setter for address field
     * @param address address
     */
    public void setAddress(String address) {
        System.out.println("setAddress Publisher.java(models)");
        this.address = address;
    }

    /**
     * getter for phone number field
     * @return phone number
     */
    public String getPhoneNumber() {
        System.out.println("getPhoneNumber Publisher.java(models)");
        return phoneNumber;
    }

    /**
     * setter for phone number field
     * @param phoneNumber phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        System.out.println("setPhoneNumber Publisher.java(models)");
        this.phoneNumber = phoneNumber;
    }
}
