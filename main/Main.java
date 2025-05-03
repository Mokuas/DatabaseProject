package main;

import main.services.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;

/**
 * This class contains entry point of the program
 */
public class Main {
    static Database database;

    /**
     * Entry point of the program
     * @param args program args
     */
    public static void main(String[] args) {
        System.out.println("Main girsi (Main.Java)");
        database = new Database();
        database.initialize();

        startApp();
    }

    /**
     * Opens a login menu
     */
    public static void startApp() {
        System.out.println("StartAPP1 (Main.java)");
        SwingUtilities.invokeLater(() -> {
            System.out.println("startApp2 (Main.Java)");
            LoginMenu loginMenu = new LoginMenu(database);
        });
    }
}