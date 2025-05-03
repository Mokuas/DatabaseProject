package main;

import main.services.*;
import main.models.User;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Login window class
 */
public class LoginMenu {
    boolean closeDbOnDispose = true;

    /**
     * Constructor for LoginMenu. Immediately opens window
     * @param database database connection
     */
    public LoginMenu(Database database) {
        System.out.println("LoginMenu.Java");
        JFrame frame = new JFrame() {
            @Override
            public void dispose() {
                System.out.println(closeDbOnDispose + " dispose (LoginMenu.java LoginMenu)");
                super.dispose();

                System.out.println(closeDbOnDispose + " LoginMenu.java LoginMenu");
                if(closeDbOnDispose) {
                    System.out.println(closeDbOnDispose + "if ici LoginMenu.java LoginMenu");
                    database.close();
                }
            }
        };

        System.out.println("userService obje LoginMenu.Java");
        UserService userService = new UserService(database.getSession());
        System.out.println("userService-librarianService obje LoginMenu.Java");
        LibrarianService librarianService = new LibrarianService(database.getSession());
        System.out.println("librarianService obje LoginMenu.Java");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Library Management System");
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);

        JComboBox<User> userField = new JComboBox<>();
        List<User> users = userService.getAll();
        userField.addItem(new User(0, "Admin User", "", "", ""));
        for(User user : users) {
            System.out.println(user + " user ismini kontrolLoginMnu.java");
            userField.addItem(user);
        }
        userField.setBounds(460, 200, 360, 30);
        frame.add(userField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(550, 300, 180, 30);
        loginButton.addActionListener(e -> {
            if(userField.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "User not selected", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            closeDbOnDispose = false;
            frame.setVisible(false);
            frame.dispose();

            // is "Admin User" or librarian
            if(userField.getSelectedIndex() == 0 || librarianService.getLibrarianByUserId(((User)userField.getSelectedItem()).getId()) != null) {
                new LibrarianGui(database);
            } else {
                new UserGui(((User) userField.getSelectedItem()).getId(), database);
            }
        });
        frame.add(loginButton);
        System.out.println("LoginMenu.Java cikis");
    }
}
