package main.dialogs;

import main.models.Librarian;
import main.models.User;
import main.services.LibrarianService;
import main.services.UserService;

import javax.swing.*;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Dialog responsible for creating/updating/deleting librarians
 */
public class CreateLibrarianDialog implements CreateDialog {
    JFrame frame;
    UserService userService;
    LibrarianService librarianService;

    Callable<Void> refreshCallback;

    int librarianId = 0;

    /**
     * Constructor
     * @param userService User Service
     * @param librarianService Librarian Service
     * @param librarianId librarian record to delete/update (pass 0 for create)
     */
    public CreateLibrarianDialog(UserService userService, LibrarianService librarianService, int librarianId) {
        System.out.println("CreateLibrarianDialog.java(dialogs)");
        this.userService = userService;
        this.librarianService = librarianService;
        this.librarianId = librarianId;
    }

    /**
     * set refresh callback
     * @param refreshCallback callback will be called when a CREATE, UPDATE or DELETE is done
     */
    public void setRefreshCallback(Callable<Void> refreshCallback) {
        System.out.println("setRefreshCallback CreateLibrarianDialog.java(dialogs)");
        this.refreshCallback = refreshCallback;
    }

    @Override
    public void setItem(Object object) {
        System.out.println("setItem CreateLibrarianDialog.java(dialogs)");
        if(object instanceof Librarian p) {
            librarianId = p.getId();
        } else {
            librarianId = 0;
        }
    }

    @Override
    public void display() {
        System.out.println("display CreateLibrarianDialog.java(dialogs)");
        Librarian librarianPlaceholder = new Librarian(0, null, Date.valueOf("2024-01-01"), "Manager");
        User userPlaceholder = new User(0, "", "", "", "");
        if(librarianId != 0) {
            librarianPlaceholder = librarianService.getById(librarianId);
            userPlaceholder = librarianPlaceholder.getUser();
        }

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setTitle(librarianId == 0 ? "Create librarian" : "Edit librarian");
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);

        JComboBox<User> userField = new JComboBox<>();
        List<User> users = userService.getAll();
        for(User user : users) {
            userField.addItem(user);
            if(user.getId() == userPlaceholder.getId()) {
                userField.setSelectedItem(user);
            }
        }
        userField.setBounds(20, 50, 360, 30);
        frame.add(userField);

        JTextField dateField = new JTextField(librarianPlaceholder.getEmploymentDate().toString());
        dateField.setToolTipText("Employment date");
        dateField.setBounds(20, 100, 360, 30);
        frame.add(dateField);


        JTextField positionField = new JTextField(librarianPlaceholder.getPosition());
        positionField.setToolTipText("Position");
        positionField.setBounds(20, 150, 360, 30);
        frame.add(positionField);

        JButton createButton = new JButton(librarianId == 0 ? "Create" : "Update");
        createButton.setBounds(50, 400, 100, 30);
        frame.add(createButton);

        createButton.addActionListener(e -> {
            try {
                Date date = Date.valueOf(dateField.getText());

                if(librarianId == 0) {
                    Librarian librarian = new Librarian(librarianId, (User)userField.getSelectedItem(), date, positionField.getText());
                    librarianService.persist(librarian);
                } else {
                    Librarian librarian = librarianService.getById(librarianId);
                    librarian.setUser((User)userField.getSelectedItem());
                    librarian.setEmploymentDate(date);
                    librarian.setPosition(positionField.getText());
                    librarianService.merge(librarian);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            } finally {
                callRefreshCallback();
            }

            frame.setVisible(false);
            frame.dispose();
        });

        if(librarianId != 0) {
            JButton deleteButton = new JButton("Delete");
            deleteButton.setBounds(150, 400, 100, 30);
            frame.add(deleteButton);

            deleteButton.addActionListener(e -> {
                librarianService.remove(librarianService.getById(librarianId));
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
        System.out.println("callRefreshCallback CreateLibrarianDialog.java(dialogs)");
        if(refreshCallback != null) {
            try {
                refreshCallback.call();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
