package main.dialogs;

import main.models.User;
import main.services.UserService;

import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Dialog responsible for creating/updating/deleting users
 */
public class CreateUserDialog implements CreateDialog {
    JFrame frame;
    UserService userService;

    Callable<Void> refreshCallback;

    int userId = 0;

    /**
     * Constructor
     * @param userService User Service
     * @param userId user record to delete/update (pass 0 for create)
     */
    public CreateUserDialog(UserService userService, int userId) {
        System.out.println("CreateUserDialog.java(dialogs)");
        this.userService = userService;
        this.userId = userId;
    }

    /**
     * set refresh callback
     * @param refreshCallback callback will be called when a CREATE, UPDATE or DELETE is done
     */
    public void setRefreshCallback(Callable<Void> refreshCallback) {//setter
        System.out.println("setRefreshCallback CreateUserDialog.java(dialogs)");
        this.refreshCallback = refreshCallback;
    }

    @Override
    public void setItem(Object object) {
        System.out.println("setItem CreateUserDialog.java(dialogs)");
        if(object instanceof User p) {
            userId = p.getId();
        } else {
            userId = 0;
        }
    }

    @Override
    public void display() {
        System.out.println("display CreateUserDialog.java(dialogs)");
        User userPlaceholder = new User(0, "Name", "Email", "Phone Number", "Address");
        if(userId != 0) {
            userPlaceholder = userService.getById(userId);
        }

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setTitle(userId == 0 ? "Create user" : "Edit user");
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);

        JTextField nameField = new JTextField(userPlaceholder.getName());
        nameField.setToolTipText("Name");
        nameField.setBounds(20, 50, 360, 30);
        frame.add(nameField);

        JTextField emailField = new JTextField(userPlaceholder.getEmail());
        emailField.setToolTipText("Email");
        emailField.setBounds(20, 100, 360, 30);
        frame.add(emailField);

        JTextField numberField = new JTextField(userPlaceholder.getPhoneNumber());
        numberField.setToolTipText("Phone Number");
        numberField.setBounds(20, 150, 360, 30);
        frame.add(numberField);

        JTextField addressField = new JTextField(userPlaceholder.getAddress());
        addressField.setToolTipText("Email");
        addressField.setBounds(20, 200, 360, 30);
        frame.add(addressField);

        JButton createButton = new JButton(userId == 0 ? "Create" : "Update");
        createButton.setBounds(50, 400, 100, 30);
        frame.add(createButton);

        createButton.addActionListener(e -> {
            try {
                User user = new User(userId, nameField.getText(), emailField.getText(), numberField.getText(), addressField.getText());
                if(userId == 0) {
                    userService.persist(user);
                } else {
                    userService.merge(user);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            callRefreshCallback();
            frame.setVisible(false);
            frame.dispose();
        });

        if(userId != 0) {
            JButton deleteButton = new JButton("Delete");
            deleteButton.setBounds(150, 400, 100, 30);
            frame.add(deleteButton);

            deleteButton.addActionListener(e -> {
                try {
                    userService.remove(userService.getById(userId));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                callRefreshCallback();

                frame.setVisible(false);
                frame.dispose();
            });
        }

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(250, 400, 100, 30);
        frame.add(cancelButton);

        cancelButton.addActionListener(e -> {
            System.out.println("Cancelled");

            frame.setVisible(false);
            frame.dispose();
        });
    }

    /**
     * Calls refresh callback
     */
    private void callRefreshCallback() {
        System.out.println("callRefreshCallback CreateUserDialog.java(dialogs)");
        if(refreshCallback != null) {
            try {
                refreshCallback.call();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
