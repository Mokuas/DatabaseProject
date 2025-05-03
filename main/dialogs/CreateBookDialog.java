package main.dialogs;

import main.models.Book;
import main.models.Publisher;
import main.services.BookService;
import main.services.PublisherService;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Dialog responsible for creating/updating/deleting books
 */
public class CreateBookDialog implements CreateDialog {
    JFrame frame;
    BookService bookService;
    PublisherService publisherService;

    Callable<Void> refreshCallback;

    int bookId = 0;

    /**
     * Constructor
     * @param bookService Book Service
     * @param publisherService Publisher Service
     * @param bookId book to delete/update (pass 0 for create)
     */
    public CreateBookDialog(BookService bookService, PublisherService publisherService, int bookId) {
        System.out.println("CreateBookDialog.java(dialogs)");
        this.bookService = bookService;
        this.publisherService = publisherService;
        this.bookId = bookId;
    }

    /**
     * set refresh callback
     * @param refreshCallback callback will be called when a CREATE, UPDATE or DELETE is done
     */
    public void setRefreshCallback(Callable<Void> refreshCallback) {
        System.out.println("setRefreshCallback (CreateBookDialog.java(dialogs))");
        this.refreshCallback = refreshCallback;
    }

    @Override
    public void setItem(Object object) {
        System.out.println("setItem (CreateBookDialog.java(dialogs))");
        System.out.println("");
        if(object instanceof Book p) {
            bookId = p.getId();
        } else {
            bookId = 0;
        }
    }

    @Override
    public void display() {
        System.out.println("display (CreateBookDialog.java(dialogs))");
        Book bookPlaceholder = new Book(0, "Book Title", "Author", null, 2000, "0-2137-6653-1");
        int publisherId = 0;
        if(bookId != 0) {
            bookPlaceholder = bookService.getById(bookId);
            publisherId = bookPlaceholder.getPublisher().getId();
        }

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setTitle(bookId == 0 ? "Create book" : "Edit book");
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);

        JComboBox<Publisher> publisherField = new JComboBox<>();
        List<Publisher> publishers = publisherService.getAll();
        for(Publisher publisher : publishers) {
            publisherField.addItem(publisher);
            if(publisher.getId() == publisherId) {
                publisherField.setSelectedItem(publisher);
            }
        }
        publisherField.setBounds(20, 50, 360, 30);
        frame.add(publisherField);

        JTextField titleField = new JTextField(bookPlaceholder.getTitle());
        titleField.setToolTipText("Book title");
        titleField.setBounds(20, 100, 360, 30);
        frame.add(titleField);

        JTextField authorField = new JTextField(bookPlaceholder.getAuthor());
        authorField.setToolTipText("Author");
        authorField.setBounds(20, 150, 360, 30);
        frame.add(authorField);

        JTextField yearField = new JTextField(Integer.toString(bookPlaceholder.getPublicationYear()));
        yearField.setToolTipText("Publication Year");
        yearField.setBounds(20, 200, 360, 30);
        frame.add(yearField);

        JTextField isbnField = new JTextField(bookPlaceholder.getIsbn());
        isbnField.setToolTipText("ISBN");
        isbnField.setBounds(20, 250, 360, 30);
        frame.add(isbnField);

        JButton createButton = new JButton(bookId == 0 ? "Create" : "Update");
        createButton.setBounds(50, 400, 100, 30);
        frame.add(createButton);

        createButton.addActionListener(e -> {
            try {
                int year = Integer.parseInt(yearField.getText());
                Publisher publisher = (Publisher) publisherField.getSelectedItem();
                if(publisher == null) {
                    throw new Exception("Publisher not selected");
                }

                if(bookId == 0) {
                    Book book = new Book(bookId, titleField.getText(), authorField.getText(), publisher, year, isbnField.getText());
                    bookService.persist(book);
                } else {
                    Book book = bookService.getById(bookId);
                    book.setTitle(titleField.getText());
                    book.setAuthor(authorField.getText());
                    book.setPublisher(publisher);
                    book.setPublicationYear(year);
                    book.setIsbn(isbnField.getText());
                    bookService.merge(book);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid publication year. " + ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            callRefreshCallback();
            frame.setVisible(false);
            frame.dispose();
        });

        if(bookId != 0) {
            JButton deleteButton = new JButton("Delete");
            deleteButton.setBounds(150, 400, 100, 30);
            frame.add(deleteButton);

            deleteButton.addActionListener(e -> {
                try {
                    bookService.remove(bookService.getById(bookId));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
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
        System.out.println("callRefreshCallback (CreateBookDialog.java(dialogs))");
        if(refreshCallback != null) {
            try {
                refreshCallback.call();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
