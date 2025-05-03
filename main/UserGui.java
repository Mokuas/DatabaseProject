package main;

import main.services.*;
import main.models.*;

import javax.swing.*;
import java.util.List;

/**
 * Window for displaying all books, available books and currently borrowed books
 */
public class UserGui {
    BookService bookService;
    BorrowingService borrowingService;

    JFrame frame;
    JList<Book> bookList;
    JList<Book> availableBookList;
    JList<Borrowing> borrowingList;

    int userId = 0;

    boolean closeDbOnDispose = true;

    /**
     * Constructor of the window
     * @param userId id of the user that logged in
     * @param database database connection
     */
    public UserGui(int userId, Database database) {
        System.out.println("UserGui.Java (UserGui.Java)");
        this.userId = userId;
        this.bookService = new BookService(database.getSession());
        this.borrowingService = new BorrowingService(database.getSession());

        frame = new JFrame() {
            @Override
            public void dispose() {
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

        bookList = addPanel(0, "All Books");
        refreshCopies();

        availableBookList = addPanel(410, "Available Books");
        refreshAvailableCopies();

        borrowingList = addPanel(820, "Borrowings");
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
     * Refreshes contents in 'All Books' list
     */
    private void refreshCopies() {
        System.out.println("refreshCopies (UserGui.Java)");
        List<Book> items = bookService.getAll();
        bookList.setListData(items.toArray(new Book[0]));
    }

    /**
     * Refreshes contents in 'Available Books' list
     */
    private void refreshAvailableCopies() {
        System.out.println("refreshAvailableCopies (UserGui.Java)");
        List<Book> items = bookService.getAvailableBooks();
        availableBookList.setListData(items.toArray(new Book[0]));
    }

    /**
     * Refreshes contents in 'Borrowings' list
     */
    private void refreshBorrowings() {
        System.out.println("refreshBorrowings (UserGui.Java)");
        List<Borrowing> items = borrowingService.getBorrowingsOfUser(userId);
        borrowingList.setListData(items.toArray(new Borrowing[0]));
    }

    /**
     * Generic function that adds a JList component with given title
     * @param xOffset horizontal offset of the created components
     * @param titleText title to display
     */
    private <Model> JList<Model> addPanel(int xOffset, String titleText) {
        System.out.println("addPanel UserGui.java");
        JLabel title = new JLabel(titleText);
        title.setBounds(60 + xOffset, 70, 350, 30);
        title.setAlignmentX(SwingConstants.CENTER);
        frame.add(title);

        JList<Model> list = new JList<>();
        list.setBounds(50+xOffset, 100, 350, 400);
        frame.add(list);
        return list;
    }
}
