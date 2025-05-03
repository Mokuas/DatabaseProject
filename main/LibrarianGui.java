package main;

import main.dialogs.*;
import main.services.*;
import main.models.*;

import java.util.List;
import javax.swing.*;

/**
 * Window for librarian panel. Manages all models
 */
public class LibrarianGui {
    UserService userService;
    LibrarianService librarianService;
    PublisherService publisherService;
    BookService bookService;
    CopyService copyService;
    BorrowingService borrowingService;

    JFrame frame;
    JList<User> userList;
    JList<Librarian> librarianList;
    JList<Publisher> publisherList;
    JList<Book> bookList;
    JList<Copy> copyList;
    JList<Borrowing> borrowingList;

    boolean closeDbOnDispose = true;

    /**
     * Constructor of the window
     * @param database database connection
     */
    public LibrarianGui(Database database) {
        System.out.println("LibrarianGui (LibrarianGui.Java)");
        this.userService = new UserService(database.getSession());
        this.librarianService = new LibrarianService(database.getSession());
        this.publisherService = new PublisherService(database.getSession());
        this.bookService = new BookService(database.getSession());
        this.copyService = new CopyService(database.getSession());
        this.borrowingService = new BorrowingService(database.getSession());

        frame = new JFrame() {
            @Override
            public void dispose() {
                System.out.println("dispose ici LibrarianGui.java");
                super.dispose();

                if(closeDbOnDispose) {
                    database.close();
                }
            }
        };

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Library Management System");
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);

        CreateUserDialog userDialog = new CreateUserDialog(userService, 0);
        userDialog.setRefreshCallback(() -> {
            System.out.println("userDialog refresh LibrarianGui.java");
            refreshUsers();
            System.out.println("userDialog.setfreshCallback lambda LibrarianGui.java");
            refreshLibrarians();
            return null;
        });
        System.out.println("CreateUserDialog lamda son LibrarianGui.java");
        userList = addPanel("user", 0, 0, userDialog);
        refreshUsers();

        CreateLibrarianDialog librarianDialog = new CreateLibrarianDialog(userService, librarianService, 0);
        librarianDialog.setRefreshCallback(() -> {
            System.out.println("librarianDialog refresh LibrarianGui.java");
            refreshLibrarians();
            return null;
        });
        System.out.println("librarianDialog lamda son LibrarianGui.java");
        librarianList = addPanel("librarian", 410, 0, librarianDialog);
        refreshLibrarians();

        CreatePublisherDialog publisherDialog = new CreatePublisherDialog(publisherService, 0);
        publisherDialog.setRefreshCallback(() -> {
            System.out.println("publisherDialog refresh LibrarianGui.java");
            refreshPublishers();
            return null;
        });
        publisherList = addPanel("publisher", 820, 0, publisherDialog);
        refreshPublishers();

        CreateBookDialog bookDialog = new CreateBookDialog(bookService, publisherService, 0);
        bookDialog.setRefreshCallback(() -> {
            refreshBooks();
            return null;
        });
        System.out.println("CreateBookDialog lamda son LibrarianGui.java");
        bookList = addPanel("book", 0, 350, bookDialog);
        refreshBooks();

        CreateCopyDialog copyDialog = new CreateCopyDialog(bookService, publisherService, copyService, 0);
        copyDialog.setRefreshCallback(() -> {
            refreshCopies();
            return null;
        });
        System.out.println("CreateCopyDialog lamda son LibrarianGui.java");
        copyList = addPanel("copy", 410, 350, copyDialog);
        refreshCopies();

        CreateBorrowingDialog borrowingDialog = new CreateBorrowingDialog(borrowingService, userService, copyService, 0);
        borrowingDialog.setRefreshCallback(() -> {
            refreshBorrowings();
            refreshCopies();
            return null;
        });
        System.out.println("CreateBorrowingDialog lamda son LibrarianGui.java");
        borrowingList = addPanel("borrowing", 820, 350, borrowingDialog);
        refreshBorrowings();

        JButton returnButton = new JButton("Menu");
        returnButton.setBounds(5, 5, 75, 30);
        frame.add(returnButton);
        returnButton.addActionListener(e -> {
            closeDbOnDispose = false;

            Main.startApp();
            frame.setVisible(false);
            frame.dispose();
        });
    }

    /**
     * Refreshes contents in 'Users' list
     */
    public void refreshUsers() {
        System.out.println("refreshUsers (LibrarianGui.Java)");
        List<User> users = userService.getAll();
        userList.setListData(users.toArray(new User[0]));
    }

    /**
     * Refreshes contents in 'Librarians' list
     */
    public void refreshLibrarians() {
        System.out.println("refreshLibrarians (LibrarianGui.Java)");
        List<Librarian> librarians = librarianService.getAll();
        librarianList.setListData(librarians.toArray(new Librarian[0]));
    }

    /**
     * Refreshes contents in 'Publishers' list
     */
    private void refreshPublishers() {
        System.out.println("refreshPublishers (LibrarianGui.Java)");
        List<Publisher> items = publisherService.getAll();
        publisherList.setListData(items.toArray(new Publisher[0]));
    }

    /**
     * Refreshes contents in 'Books' list
     */
    private void refreshBooks() {
        System.out.println("refreshBooks (LibrarianGui.Java)");
        List<Book> items = bookService.getAll();
        bookList.setListData(items.toArray(new Book[0]));
    }

    /**
     * Refreshes contents in 'Copies' list
     */
    private void refreshCopies() {
        System.out.println("refreshCopies (LibrarianGui.Java)");
        List<Copy> items = copyService.getAll();
        copyList.setListData(items.toArray(new Copy[0]));
    }

    /**
     * Refreshes contents in 'Borrowings' list
     */
    private void refreshBorrowings() {
        System.out.println("refreshBorrowings (LibrarianGui.Java)");
        List<Borrowing> items = borrowingService.getAll();
        borrowingList.setListData(items.toArray(new Borrowing[0]));
    }

    /**
     * Generic method for creating management buttons and list for given model
     * @param modelName name of the model buttons operate on. e.g. "book", "librarian"
     * @param xOffset horizontal offset of created components
     * @param yOffset vertical offset of created components
     * @param dialog dialog will be shown in create/update button interactions
     * @return created JList element
     * @param <Model> type of the model components are responsible for
     * @param <UserDialog> type of the class buttons will generate
     */
    private <Model, UserDialog extends CreateDialog> JList<Model> addPanel(String modelName, int xOffset, int yOffset, UserDialog dialog) {
        System.out.println("addPanel (LibrarianGui.Java)");
        JButton btnCreate = new JButton("Create " + modelName);
        btnCreate.setBounds(125 + xOffset, 20 + yOffset, 200, 30);
        btnCreate.addActionListener(e -> {
            System.out.println("Button giris addPanel LibrarianGui.java");
            dialog.setItem(null);
            dialog.display();
        });
        frame.add(btnCreate);

        JList<Model> list = new JList<>();
        list.setBounds(50+xOffset, 60 + yOffset, 350, 200);
        frame.add(list);

        JButton btnUpdate = new JButton("Edit/Delete " + modelName);
        btnUpdate.setBounds(125 + xOffset, 270 + yOffset, 200, 30);
        btnUpdate.addActionListener(e -> {
            if(list.getSelectedValue() == null) {
                JOptionPane.showMessageDialog(null, modelName + " not selected", "Invalid " + modelName, JOptionPane.ERROR_MESSAGE);
                return;
            }
            dialog.setItem(list.getSelectedValue());
            dialog.display();
        });
        frame.add(btnUpdate);

        return list;
    }
}
