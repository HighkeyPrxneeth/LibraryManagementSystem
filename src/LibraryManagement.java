package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.NumberFormatException;

// Main class for the Library Management System
class LibraryManagement {
    private JFrame mainFrame; // Main application window
    private JPanel currentPanel; // Current panel being displayed
    private List<Book> books; // List of books in the library
    private Map<String, User> users; // Map of users with their credentials
    private User currentUser; // Currently logged-in user

    private DefaultListModel<Book> listModel = new DefaultListModel<>(); // List model for the book list
    private JList<Book> bookList = new JList<>(listModel); // JList to display the book list

    // Constructor to initialize the library management system
    public LibraryManagement() {
        books = new ArrayList<>();
        users = new HashMap<>();
        initializeUsers(); // Initialize default users
        initializeGUI(); // Initialize the graphical user interface
    }

    // Method to initialize default users
    public void initializeUsers() {
        users.put("user1", new User("user1", "password1"));
        users.put("user2", new User("user2", "password2"));
    }

    // Method to initialize the graphical user interface
    public void initializeGUI() {
        mainFrame = new JFrame("Library Management System");
        mainFrame.setSize(1200, 800);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        showLoginPanel(); // Show the login panel initially
        mainFrame.setVisible(true);
    }

    // Method to display the login panel
    public void showLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        // Action listener for the login button
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            if (authenticate(username, password)) { // Authenticate user
                currentUser = users.get(username);
                showLibraryPanel(); // Show library panel on successful login
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action listener for the register button
        registerButton.addActionListener(e -> showRegistrationPanel());

        // Adding components to the login panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 3;
        loginPanel.add(registerButton, gbc);

        setCurrentPanel(loginPanel); // Set the current panel to login panel
    }

    // Method to display the registration panel
    public void showRegistrationPanel() {
        JPanel registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JButton registerButton = new JButton("Register");

        // Action listener for the register button
        registerButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            if (users.containsKey(username)) {
                JOptionPane.showMessageDialog(mainFrame, "Username already exists", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            } else {
                users.put(username, new User(username, password));
                JOptionPane.showMessageDialog(mainFrame, "Registration successful", "Registration", JOptionPane.INFORMATION_MESSAGE);
                showLoginPanel(); // Show login panel after successful registration
            }
        });

        // Adding components to the registration panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        registerPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        registerPanel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        registerPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        registerPanel.add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        registerPanel.add(registerButton, gbc);

        setCurrentPanel(registerPanel); // Set the current panel to registration panel
    }

    // Method to authenticate user credentials
    public boolean authenticate(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    // Method to display the library panel
    public void showLibraryPanel() {
        JPanel libraryPanel = new JPanel(new BorderLayout());
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Inventory", createInventoryPanel()); // Tab for inventory management
        tabbedPane.addTab("Search", createSearchPanel()); // Tab for searching books

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            currentUser = null;
            showLoginPanel(); // Show login panel on logout
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Welcome, " + currentUser.getUsername()), BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);

        libraryPanel.add(topPanel, BorderLayout.NORTH);
        libraryPanel.add(tabbedPane, BorderLayout.CENTER);

        setCurrentPanel(libraryPanel); // Set the current panel to library panel
    }

    // Method to create the inventory panel
    public JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnField = new JTextField();
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Available", "Not Available"});
        JButton addButton = new JButton("Add Book");

        // Adding components to the input panel
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author:"));
        inputPanel.add(authorField);
        inputPanel.add(new JLabel("ISBN-13 (numeric only):"));
        inputPanel.add(isbnField);
        inputPanel.add(new JLabel("Status:"));
        inputPanel.add(statusCombo);
        inputPanel.add(new JLabel());
        inputPanel.add(addButton);

        JScrollPane scrollPane = new JScrollPane(bookList);
        
        // Action listener for the add button
        addButton.addActionListener(e -> {
            try {
                long isbn = Long.parseLong(isbnField.getText());
                Book book = new Book(titleField.getText(), authorField.getText(), isbn, statusCombo.getSelectedItem().toString());
                books.add(book);
                listModel.addElement(book);
                clearFields(titleField, authorField, isbnField); // Clear input fields after adding book
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Please enter a valid ISBN-13", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Method to create the search panel
    public JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        DefaultListModel<Book> listModel = new DefaultListModel<>();
        JList<Book> resultList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(resultList);

        // Action listener for the search button
        searchButton.addActionListener(e -> {
            String query = searchField.getText().toLowerCase();
            listModel.clear();
            for (Book book : books) {
                if (book.getTitle().toLowerCase().contains(query) || 
                    book.getAuthor().toLowerCase().contains(query) ||
                    String.valueOf(book.getIsbn()).contains(query)) {
                    listModel.addElement(book);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton borrowButton = new JButton("Borrow Selected Book");
        JButton returnButton = new JButton("Return Selected Book");
        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);

        // Action listener for the borrow button
        borrowButton.addActionListener(e -> {
            Book selectedBook = resultList.getSelectedValue();
            if (selectedBook != null) {
                if ("Available".equals(selectedBook.getStatus())) {
                    selectedBook.setStatus("Not Available");
                    selectedBook.setBorrowedBy(currentUser.getUsername());
                    JOptionPane.showMessageDialog(mainFrame, "Book borrowed successfully!");
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Book is not available for borrowing.");
                }
                resultList.repaint();
            }
        });

        // Action listener for the return button
        returnButton.addActionListener(e -> {
            Book selectedBook = resultList.getSelectedValue();
            if (selectedBook != null) {
                if ("Not Available".equals(selectedBook.getStatus()) && 
                    currentUser.getUsername().equals(selectedBook.getBorrowedBy())) {
                    selectedBook.setStatus("Available");
                    selectedBook.setBorrowedBy(null);
                    JOptionPane.showMessageDialog(mainFrame, "Book returned successfully!");
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "You cannot return this book.");
                }
                resultList.repaint();
            }
        });

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Method to set the current panel being displayed
    public void setCurrentPanel(JPanel panel) {
        if (currentPanel != null) {
            mainFrame.remove(currentPanel);
        }
        currentPanel = panel;
        mainFrame.add(currentPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // Method to clear input fields
    public void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    // Method to add books to the library while initializing
    public void initBooks() {
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", 9780743273565L, "Available"));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", 9780061120084L, "Available"));
        books.add(new Book("1984", "George Orwell", 9780451524935L, "Available"));
        books.add(new Book("Pride and Prejudice", "Jane Austen", 9780679783268L, "Available"));
        books.add(new Book("The Catcher in the Rye", "J.D. Salinger", 9780316769488L, "Available"));
        books.add(new Book("The Hobbit", "J.R.R. Tolkien", 9780345534835L, "Available"));
        books.add(new Book("The Lord of the Rings", "J.R.R. Tolkien", 9780544003415L, "Available"));
        books.add(new Book("Animal Farm", "George Orwell", 9780451526342L, "Available"));
        books.add(new Book("Brave New World", "Aldous Huxley", 9780060850524L, "Available"));
        books.add(new Book("The Grapes of Wrath", "John Steinbeck", 9780143039433L, "Available"));
        listModel.addAll(books);
    }
}
