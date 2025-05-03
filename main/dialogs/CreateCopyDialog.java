package main.dialogs;

import main.models.Book;
import main.models.Copy;
import main.models.CopyStatus;
import main.services.BookService;
import main.services.CopyService;
import main.services.PublisherService;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Dialog responsible for creating/updating/deleting copies
 */
public class CreateCopyDialog implements CreateDialog {
    JFrame frame;
    BookService bookService;
    CopyService copyService;
    PublisherService publisherService;

    Callable<Void> refreshCallback;

    int copyId = 0;

    /**
     * Constructor
     * @param bookService Book Service
     * @param publisherService Publisher Service
     * @param copyService Copy Service
     * @param copyId copy record to delete/update (pass 0 for create)
     */
    public CreateCopyDialog(BookService bookService, PublisherService publisherService, CopyService copyService, int copyId) {
        System.out.println("CreateCopyDialog.java(dialogs)");
        this.bookService = bookService;
        this.publisherService = publisherService;
        this.copyService = copyService;
        this.copyId = copyId;
    }

    /**
     * set refresh callback
     * @param refreshCallback callback will be called when a CREATE, UPDATE or DELETE is done
     */
    public void setRefreshCallback(Callable<Void> refreshCallback) {
        System.out.println("setRefreshCallback CreateCopyDialog.java(dialogs)");
        this.refreshCallback = refreshCallback;
    }

    @Override
    public void setItem(Object object) {
        System.out.println("setItem CreateCopyDialog.java(dialogs)");
        if(object instanceof Copy p) {
            copyId = p.getId();
        } else {
            copyId = 0;
        }
    }

    @Override
    public void display() {
        System.out.println("display CreateCopyDialog.java(dialogs)");
        Copy copyPlaceholder = new Copy(0, null, 0, CopyStatus.AVAILABLE);
        String status;
        int bookId = 0;
        if(copyId != 0) {
            copyPlaceholder = copyService.getById(copyId);
            status = copyPlaceholder.getStatus();
            bookId = copyPlaceholder.getBook().getId();
        } else {
            status = CopyStatus.AVAILABLE;
        }

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setTitle(copyId == 0 ? "Create copy" : "Edit copy");
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);

        JComboBox<Book> bookField = new JComboBox<>();
        List<Book> publishers = bookService.getAll();
        for(Book book : publishers) {
            bookField.addItem(book);
            if(book.getId() == bookId) {
                bookField.setSelectedItem(book);
            }
        }
        bookField.setBounds(20, 50, 360, 30);
        frame.add(bookField);

        JTextField copyNumberField = new JTextField(Integer.toString(copyPlaceholder.getCopyNumber()));
        copyNumberField.setToolTipText("Copy number");
        copyNumberField.setBounds(20, 100, 360, 30);
        frame.add(copyNumberField);

        JButton createButton = new JButton(copyId == 0 ? "Create" : "Update");
        createButton.setBounds(50, 400, 100, 30);
        frame.add(createButton);

        createButton.addActionListener(e -> {
            try {
                int copyNumber = Integer.parseInt(copyNumberField.getText());
                Book book = (Book) bookField.getSelectedItem();
                if(book == null) {
                    throw new Exception("Book not selected");
                }

                Copy copy = new Copy(copyId, book, copyNumber, status);
                if(copyId == 0) {
                    copyService.persist(copy);
                } else {
                    copyService.merge(copy);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid copy number. " + ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            callRefreshCallback();
            frame.setVisible(false);
            frame.dispose();
        });

        if(copyId != 0) {
            JButton deleteButton = new JButton("Delete");
            deleteButton.setBounds(150, 400, 100, 30);
            frame.add(deleteButton);

            deleteButton.addActionListener(e -> {
                try {
                    copyService.remove(copyService.getById(copyId));
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
        System.out.println("callRefreshCallback CreateCopyDialog.java(dialogs)");
        if(refreshCallback != null) {
            try {
                refreshCallback.call();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
