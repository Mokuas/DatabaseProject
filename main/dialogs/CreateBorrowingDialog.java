package main.dialogs;

import main.models.Borrowing;
import main.models.Copy;
import main.models.User;
import main.services.BorrowingService;
import main.services.CopyService;
import main.services.UserService;

import javax.swing.*;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Dialog responsible for creating/updating/deleting borrowings
 */
public class CreateBorrowingDialog implements CreateDialog {
    JFrame frame;

    CopyService copyService;
    UserService userService;
    BorrowingService borrowingService;

    Callable<Void> refreshCallback;

    int borrowingId = 0;

    /**
     * Constructor
     * @param borrowingService Borrowing Service
     * @param userService User Service
     * @param copyService Copy Service
     * @param borrowingId borrowing record to delete/update (pass 0 for create)
     */
    public CreateBorrowingDialog(BorrowingService borrowingService, UserService userService, CopyService copyService, int borrowingId) {
        System.out.println("CreateBorrowingDialog.java(dialogs)");
        this.borrowingService = borrowingService;
        this.userService = userService;
        this.copyService = copyService;
        this.borrowingId = borrowingId;
    }

    /**
     * set refresh callback
     * @param refreshCallback callback will be called when a CREATE, UPDATE or DELETE is done
     */
    public void setRefreshCallback(Callable<Void> refreshCallback) {
        System.out.println("setRefreshCallback (CreateBorrowingDialog.java(dialogs))");
        this.refreshCallback = refreshCallback;
    }

    @Override
    public void setItem(Object object) {
        System.out.println("setItem (CreateBorrowingDialog.java(dialogs))");
        if(object instanceof Borrowing p) {
            borrowingId = p.getId();
        } else {
            borrowingId = 0;
        }
    }

    @Override
    public void display() {
        System.out.println("display CreateBorrowingDialog.java(dialogs)");
        Borrowing borrowingPlaceholder = new Borrowing(0, null, null, Date.valueOf("2000-12-30"), null);
        int userIdPlaceholder = 0;
        int copyIdPlaceholder = 0;
        String returnDateString = "";
        if(borrowingId != 0) {
            borrowingPlaceholder = borrowingService.getById(borrowingId);
            userIdPlaceholder = borrowingPlaceholder.getUser().getId();
            copyIdPlaceholder = borrowingPlaceholder.getCopy().getId();
            if(borrowingPlaceholder.getReturnDate() != null) {
                returnDateString = borrowingPlaceholder.getReturnDate().toString();
            }
        }

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setTitle(borrowingId == 0 ? "Create borrowing" : "Edit borrowing");
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);

        JComboBox<User> userField = new JComboBox<>();
        List<User> users = userService.getAll();
        for(User user : users) {
            userField.addItem(user);
            if(user.getId() == userIdPlaceholder) {
                userField.setSelectedItem(user);
            }
        }
        userField.setBounds(20, 50, 360, 30);
        frame.add(userField);

        JComboBox<Copy> copyField = new JComboBox<>();
        List<Copy> copies = copyService.getAll();
        for(Copy copy : copies) {
            copyField.addItem(copy);
            if(copy.getId() == copyIdPlaceholder) {
                copyField.setSelectedItem(copy);
            }
        }
        copyField.setBounds(20, 100, 360, 30);
        frame.add(copyField);

        JTextField borrowDateField = new JTextField(borrowingPlaceholder.getBorrowDate().toString());
        borrowDateField.setToolTipText("Borrow date");
        borrowDateField.setBounds(20, 150, 360, 30);
        frame.add(borrowDateField);

        JTextField returnDateField = new JTextField(returnDateString);
        returnDateField.setToolTipText("Return date");
        returnDateField.setBounds(20, 200, 360, 30);
        frame.add(returnDateField);

        JButton createButton = new JButton(borrowingId == 0 ? "Create" : "Update");
        createButton.setBounds(50, 400, 100, 30);
        frame.add(createButton);

        createButton.addActionListener(e -> {
            try {
                Date borowDate = Date.valueOf(borrowDateField.getText());
                Date returnDate = null;
                if(!returnDateField.getText().isEmpty()) {
                    returnDate = Date.valueOf(returnDateField.getText());
                }

                User user = (User) userField.getSelectedItem();
                if(user == null) {
                    throw new Exception("User not selected");
                }

                Copy copy = (Copy) copyField.getSelectedItem();
                if(copy == null) {
                    throw new Exception("Copy not selected");
                }

                Borrowing borrowing = new Borrowing(borrowingId, user, copy, borowDate, returnDate);
                if(borrowingId == 0) {
                    borrowingService.persist(borrowing);
                } else {
                    borrowingService.merge(borrowing);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Invalid date value. " + ex.getMessage() + ". Format should be YYYY-MM-DD", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            callRefreshCallback();
            frame.setVisible(false);
            frame.dispose();
        });

        if(borrowingId != 0) {
            JButton deleteButton = new JButton("Delete");
            deleteButton.setBounds(150, 400, 100, 30);
            frame.add(deleteButton);

            deleteButton.addActionListener(e -> {
                try {
                    borrowingService.remove(borrowingService.getById(borrowingId));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                } finally {
                    callRefreshCallback();
                }

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
        System.out.println("callRefreshCallback CreateBorrowingDialog.java(dialogs)");
        if(refreshCallback != null) {
            try {
                refreshCallback.call();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
