// Main.java
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Main {
    private List<Book> books;
    private List<User> users;
    private List<Transaction> transactions;
    private User loggedInUser;
    private Scanner scanner;
    
    public Main() {
        books = new ArrayList<>();
        users = new ArrayList<>();
        transactions = new ArrayList<>();
        scanner = new Scanner(System.in);
    }
    
    // Load data from files
    public void loadData() {
        loadUsers();
        loadBooks();
        loadTransactions();
    }
    
    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    users.add(new User(parts[0], parts[1], parts[2], parts[3]));
                }
            }
            System.out.println("Users loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("users.txt not found. Creating new file...");
            createDefaultUsers();
        } catch (IOException e) {
            System.out.println("Error reading users.txt: " + e.getMessage());
        }
    }
    
    private void loadBooks() {
        try (BufferedReader br = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    books.add(new Book(parts[0], parts[1], parts[2], Boolean.parseBoolean(parts[3])));
                }
            }
            System.out.println("Books loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("books.txt not found. Creating new file...");
            createDefaultBooks();
        } catch (IOException e) {
            System.out.println("Error reading books.txt: " + e.getMessage());
        }
    }
    
    private void loadTransactions() {
        try (BufferedReader br = new BufferedReader(new FileReader("transactions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    transactions.add(new Transaction(parts[0], parts[1], parts[2], parts[3], parts[4]));
                    
                    // Update user's borrowed books list
                    if (parts[4].equals("null")) {
                        User user = findUserById(parts[1]);
                        if (user != null) {
                            user.addBorrowedBook(parts[2]);
                        }
                    }
                }
            }
            System.out.println("Transactions loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("transactions.txt not found. Creating new file...");
        } catch (IOException e) {
            System.out.println("Error reading transactions.txt: " + e.getMessage());
        }
    }
    
    private void createDefaultUsers() {
        users.add(new User("1", "james", "123", "user"));
        users.add(new User("2", "cultura", "123", "user"));
        users.add(new User("1", "saldon", "123", "admin"));
        saveUsers();
    }
    
    private void createDefaultBooks() {
        books.add(new Book("B001", "The Great Gatsby", "F. Scott Fitzgerald", true));
        books.add(new Book("B002", "To Kill a Mockingbird", "Harper Lee", true));
        books.add(new Book("B003", "1984", "George Orwell", false));
        saveBooks();
    }
    
    // Save data to files
    public void saveAllData() {
        saveUsers();
        saveBooks();
        saveTransactions();
    }
    
    private void saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("users.txt"))) {
            for (User user : users) {
                pw.println(user.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
    
    private void saveBooks() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("books.txt"))) {
            for (Book book : books) {
                pw.println(book.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }
    
    private void saveTransactions() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("transactions.txt"))) {
            for (Transaction transaction : transactions) {
                pw.println(transaction.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }
    
    // Login system
    public boolean login() {
        System.out.println("\n========================================");
        System.out.println("Welcome to the Library Management System");
        System.out.println("========================================");
        System.out.println("Please log in to continue.\n");
        
        int attempts = 3;
        
        while (attempts > 0) {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            
            User user = authenticateUser(username, password);
            
            if (user != null) {
                loggedInUser = user;
                System.out.println("\nLogin successful! Welcome, " + user.getName() + ".");
                return true;
            } else {
                attempts--;
                if (attempts > 0) {
                    System.out.println("Invalid username or password. Try again.");
                    System.out.println("(Attempts left: " + attempts + ")\n");
                } else {
                    System.out.println("Maximum login attempts exceeded. Exiting...");
                    return false;
                }
            }
        }
        
        return false;
    }
    
    private User authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    
    // Main menu display
    public void displayMenu() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("           LIBRARY MAIN MENU");
            System.out.println("========================================");
            System.out.println("1. View All Books");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            
            if (loggedInUser.getRole().equals("admin")) {
                System.out.println("4. Manage Users");
                System.out.println("5. Manage Catalogue");
                System.out.println("6. View Transactions");
            }
            
            System.out.println("0. Exit");
            System.out.println("========================================");
            System.out.print("Enter choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        viewAllBooks();
                        break;
                    case 2:
                        borrowBook();
                        break;
                    case 3:
                        returnBook();
                        break;
                    case 4:
                        if (loggedInUser.getRole().equals("admin")) {
                            manageUsers();
                        } else {
                            System.out.println("Invalid choice. Please try again.");
                        }
                        break;
                    case 5:
                        if (loggedInUser.getRole().equals("admin")) {
                            manageCatalogue();
                        } else {
                            System.out.println("Invalid choice. Please try again.");
                        }
                        break;
                    case 6:
                        if (loggedInUser.getRole().equals("admin")) {
                            manageTransactions();
                        } else {
                            System.out.println("Invalid choice. Please try again.");
                        }
                        break;
                    case 0:
                        System.out.println("\nSaving data and exiting...");
                        saveAllData();
                        System.out.println("Thank you for using the Library Management System!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    // View all books
    private void viewAllBooks() {
        System.out.println("\n========================================");
        System.out.println("           ALL BOOKS");
        System.out.println("========================================");
        
        if (books.isEmpty()) {
            System.out.println("No books available in the library.");
        } else {
            for (Book book : books) {
                book.displayBookDetails();
            }
        }
        
        System.out.println("========================================");
    }
    
    // Borrow book
    private void borrowBook() {
        System.out.println("\n========================================");
        System.out.println("           BORROW BOOK");
        System.out.println("========================================");
        
        // Check if user has reached borrowing limit
        if (loggedInUser.getBorrowedBooks().size() >= 3) {
            System.out.println("You have reached the maximum borrowing limit of 3 books.");
            System.out.println("Please return a book before borrowing another.");
            return;
        }
        
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();
        
        try {
            Book book = findBookById(bookId);
            
            if (book == null) {
                throw new NullPointerException("Book not found.");
            }
            
            if (!book.isAvailable()) {
                throw new BookUnavailableException("This book is currently unavailable.");
            }
            
            // Create transaction
            String transactionId = generateTransactionId();
            String currentDate = LocalDate.now().toString();
            Transaction transaction = new Transaction(transactionId, loggedInUser.getId(), bookId, currentDate, "null");
            transactions.add(transaction);
            
            // Update book and user
            book.setAvailable(false);
            loggedInUser.addBorrowedBook(bookId);
            
            System.out.println("\nBook borrowed successfully!");
            System.out.println("Transaction ID: " + transactionId);
            book.displayBookDetails();
            
        } catch (NullPointerException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (BookUnavailableException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println("\n========================================");
        System.out.println("Returning to main menu...");
    }
    
    // Return book
    private void returnBook() {
        System.out.println("\n========================================");
        System.out.println("           RETURN BOOK");
        System.out.println("========================================");
        
        if (loggedInUser.getBorrowedBooks().isEmpty()) {
            System.out.println("You have no books to return.");
            return;
        }
        
        System.out.println("Your borrowed books:");
        for (String bookId : loggedInUser.getBorrowedBooks()) {
            Book book = findBookById(bookId);
            if (book != null) {
                book.displayBookDetails();
            }
        }
        
        System.out.print("\nEnter Book ID to return: ");
        String bookId = scanner.nextLine();
        
        try {
            if (!loggedInUser.getBorrowedBooks().contains(bookId)) {
                throw new NullPointerException("You have not borrowed this book.");
            }
            
            Book book = findBookById(bookId);
            if (book == null) {
                throw new NullPointerException("Book not found.");
            }
            
            // Find and update transaction
            Transaction transaction = findActiveTransaction(loggedInUser.getId(), bookId);
            if (transaction != null) {
                transaction.setDateReturned(LocalDate.now().toString());
            }
            
            // Update book and user
            book.setAvailable(true);
            loggedInUser.removeBorrowedBook(bookId);
            
            System.out.println("\nBook returned successfully!");
            book.displayBookDetails();
            
        } catch (NullPointerException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println("\n========================================");
        System.out.println("Returning to main menu...");
    }
    
    // Manage Users (Admin only)
    private void manageUsers() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("         USER MANAGEMENT");
            System.out.println("========================================");
            System.out.println("1. Add User");
            System.out.println("2. Update User");
            System.out.println("3. Delete User");
            System.out.println("4. Display All Users");
            System.out.println("0. Back to Main Menu");
            System.out.println("========================================");
            System.out.print("Enter choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        addUser();
                        break;
                    case 2:
                        updateUser();
                        break;
                    case 3:
                        deleteUser();
                        break;
                    case 4:
                        displayAllUsers();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    private void addUser() {
        System.out.println("\n--- Add New User ---");
        System.out.print("Enter User ID (e.g., U003): ");
        String id = scanner.nextLine();
        
        if (findUserById(id) != null) {
            System.out.println("User ID already exists!");
            return;
        }
        
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Role (user/admin): ");
        String role = scanner.nextLine();
        
        users.add(new User(id, name, password, role));
        System.out.println("User added successfully!");
        
        // Demonstrate polymorphism
        Person p = new User(id, name, password, role);
        System.out.print("Polymorphic display: ");
        p.displayInfo();
    }
    
    private void updateUser() {
        System.out.println("\n--- Update User ---");
        System.out.print("Enter User ID to update: ");
        String id = scanner.nextLine();
        
        User user = findUserById(id);
        if (user == null) {
            System.out.println("User not found!");
            return;
        }
        
        System.out.println("Current details:");
        user.displayInfo();
        
        System.out.print("Enter new Name (or press Enter to skip): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            user.setName(name);
        }
        
        System.out.print("Enter new Password (or press Enter to skip): ");
        String password = scanner.nextLine();
        if (!password.isEmpty()) {
            user.setPassword(password);
        }
        
        System.out.print("Enter new Role (or press Enter to skip): ");
        String role = scanner.nextLine();
        if (!role.isEmpty()) {
            user.setRole(role);
        }
        
        System.out.println("User updated successfully!");
    }
    
    private void deleteUser() {
        System.out.println("\n--- Delete User ---");
        System.out.print("Enter User ID to delete: ");
        String id = scanner.nextLine();
        
        User user = findUserById(id);
        if (user == null) {
            System.out.println("User not found!");
            return;
        }
        
        if (user.getId().equals(loggedInUser.getId())) {
            System.out.println("You cannot delete your own account!");
            return;
        }
        
        users.remove(user);
        System.out.println("User deleted successfully!");
    }
    
    private void displayAllUsers() {
        System.out.println("\n========================================");
        System.out.println("           ALL USERS");
        System.out.println("========================================");
        
        for (User user : users) {
            user.displayInfo();
        }
        
        System.out.println("========================================");
    }
    
    // Manage Catalogue (Admin only)
    private void manageCatalogue() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("       CATALOGUE MANAGEMENT");
            System.out.println("========================================");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Delete Book");
            System.out.println("4. Display All Books");
            System.out.println("5. Search Books");
            System.out.println("0. Back to Main Menu");
            System.out.println("========================================");
            System.out.print("Enter choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        addBook();
                        break;
                    case 2:
                        updateBook();
                        break;
                    case 3:
                        deleteBook();
                        break;
                    case 4:
                        viewAllBooks();
                        break;
                    case 5:
                        searchBooks();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    private void addBook() {
        System.out.println("\n--- Add New Book ---");
        System.out.print("Enter Book ID (e.g., B004): ");
        String id = scanner.nextLine();
        
        if (findBookById(id) != null) {
            System.out.println("Book ID already exists!");
            return;
        }
        
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        
        books.add(new Book(id, title, author, true));
        System.out.println("Book added successfully!");
    }
    
    private void updateBook() {
        System.out.println("\n--- Update Book ---");
        System.out.print("Enter Book ID to update: ");
        String id = scanner.nextLine();
        
        Book book = findBookById(id);
        if (book == null) {
            System.out.println("Book not found!");
            return;
        }
        
        System.out.println("Current details:");
        book.displayBookDetails();
        
        System.out.print("Enter new Title (or press Enter to skip): ");
        String title = scanner.nextLine();
        if (!title.isEmpty()) {
            book.setTitle(title);
        }
        
        System.out.print("Enter new Author (or press Enter to skip): ");
        String author = scanner.nextLine();
        if (!author.isEmpty()) {
            book.setAuthor(author);
        }
        
        System.out.println("Book updated successfully!");
    }
    
    private void deleteBook() {
        System.out.println("\n--- Delete Book ---");
        System.out.print("Enter Book ID to delete: ");
        String id = scanner.nextLine();
        
        Book book = findBookById(id);
        if (book == null) {
            System.out.println("Book not found!");
            return;
        }
        
        if (!book.isAvailable()) {
            System.out.println("Cannot delete a borrowed book!");
            return;
        }
        
        books.remove(book);
        System.out.println("Book deleted successfully!");
    }
    
    private void searchBooks() {
        System.out.println("\n--- Search Books ---");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author");
        System.out.print("Enter choice: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter search term: ");
            String searchTerm = scanner.nextLine().toLowerCase();
            
            System.out.println("\n--- Search Results ---");
            boolean found = false;
            
            for (Book book : books) {
                boolean match = false;
                
                if (choice == 1 && book.getTitle().toLowerCase().contains(searchTerm)) {
                    match = true;
                } else if (choice == 2 && book.getAuthor().toLowerCase().contains(searchTerm)) {
                    match = true;
                }
                
                if (match) {
                    book.displayBookDetails();
                    found = true;
                }
            }
            
            if (!found) {
                System.out.println("No books found matching your search.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
    
    // Manage Transactions (Admin only)
    private void manageTransactions() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("      TRANSACTION MANAGEMENT");
            System.out.println("========================================");
            System.out.println("1. View All Transactions");
            System.out.println("2. View Transactions by User");
            System.out.println("3. View Transactions by Book");
            System.out.println("0. Back to Main Menu");
            System.out.println("========================================");
            System.out.print("Enter choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        viewAllTransactions();
                        break;
                    case 2:
                        viewTransactionsByUser();
                        break;
                    case 3:
                        viewTransactionsByBook();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    private void viewAllTransactions() {
        System.out.println("\n========================================");
        System.out.println("        ALL TRANSACTIONS");
        System.out.println("========================================");
        
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction transaction : transactions) {
                transaction.displayTransaction();
            }
        }
        
        System.out.println("========================================");
    }
    
    private void viewTransactionsByUser() {
        System.out.print("\nEnter User ID: ");
        String userId = scanner.nextLine();
        
        System.out.println("\n--- Transactions for User " + userId + " ---");
        boolean found = false;
        
        for (Transaction transaction : transactions) {
            if (transaction.getUserId().equals(userId)) {
                transaction.displayTransaction();
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No transactions found for this user.");
        }
    }
    
    private void viewTransactionsByBook() {
        System.out.print("\nEnter Book ID: ");
        String bookId = scanner.nextLine();
        
        System.out.println("\n--- Transactions for Book " + bookId + " ---");
        boolean found = false;
        
        for (Transaction transaction : transactions) {
            if (transaction.getBookId().equals(bookId)) {
                transaction.displayTransaction();
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No transactions found for this book.");
        }
    }
    
    // Helper methods
    private User findUserById(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
    
    private Book findBookById(String id) {
        for (Book book : books) {
            if (book.getBookId().equals(id)) {
                return book;
            }
        }
        return null;
    }
    
    private Transaction findActiveTransaction(String userId, String bookId) {
        for (Transaction transaction : transactions) {
            if (transaction.getUserId().equals(userId) && 
                transaction.getBookId().equals(bookId) && 
                transaction.getDateReturned().equals("null")) {
                return transaction;
            }
        }
        return null;
    }
    
    private String generateTransactionId() {
        int maxId = 0;
        for (Transaction transaction : transactions) {
            String id = transaction.getTransactionId();
            int num = Integer.parseInt(id.substring(1));
            if (num > maxId) {
                maxId = num;
            }
        }
        return String.format("T%03d", maxId + 1);
    }
    
    // Main method
    public static void main(String[] args) {
        Main library = new Main();
        library.loadData();
        
        if (library.login()) {
            library.displayMenu();
        }
    }
}

// Person.java - Base Class
class Person {
    protected String id;
    protected String name;
    
    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public void displayInfo() {
        System.out.println("ID: " + id + ", Name: " + name);
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// User.java - Subclass of Person
class User extends Person {
    private String password;
    private String role;
    private ArrayList<String> borrowedBooks;
    
    public User(String id, String name, String password, String role) {
        super(id, name);
        this.password = password;
        this.role = role;
        this.borrowedBooks = new ArrayList<>();
    }
    
    @Override
    public void displayInfo() {
        System.out.println("UserID: " + id + " | Name: " + name + " | Role: " + role + " | Books Borrowed: " + borrowedBooks.size());
    }
    
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public ArrayList<String> getBorrowedBooks() { return borrowedBooks; }
    
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    
    public void addBorrowedBook(String bookId) {
        borrowedBooks.add(bookId);
    }
    
    public void removeBorrowedBook(String bookId) {
        borrowedBooks.remove(bookId);
    }
    
    public String toFileString() {
        return id + "," + name + "," + password + "," + role;
    }
}

// Book.java
class Book {
    private String bookId;
    private String title;
    private String author;
    private boolean available;
    
    public Book(String bookId, String title, String author, boolean available) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.available = available;
    }
    
    public void displayBookDetails() {
        String status = available ? "Available" : "Borrowed";
        System.out.println("BookID: " + bookId + " | Title: " + title + " | Author: " + author + " | Status: " + status);
    }
    
    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return available; }
    
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public String toFileString() {
        return bookId + "," + title + "," + author + "," + available;
    }
}

// Transaction.java
class Transaction {
    private String transactionId;
    private String userId;
    private String bookId;
    private String dateBorrowed;
    private String dateReturned;
    
    public Transaction(String transactionId, String userId, String bookId, String dateBorrowed, String dateReturned) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.bookId = bookId;
        this.dateBorrowed = dateBorrowed;
        this.dateReturned = dateReturned;
    }
    
    public void displayTransaction() {
        String returnStatus = dateReturned.equals("null") ? "Not Returned" : dateReturned;
        System.out.println("TransactionID: " + transactionId + " | UserID: " + userId + " | BookID: " + bookId + " | Borrowed: " + dateBorrowed + " | Returned: " + returnStatus);
    }
    
    public String getTransactionId() { return transactionId; }
    public String getUserId() { return userId; }
    public String getBookId() { return bookId; }
    public String getDateBorrowed() { return dateBorrowed; }
    public String getDateReturned() { return dateReturned; }
    
    public void setDateReturned(String dateReturned) { this.dateReturned = dateReturned; }
    
    public String toFileString() {
        return transactionId + "," + userId + "," + bookId + "," + dateBorrowed + "," + dateReturned;
    }
}

// Custom Exception
class BookUnavailableException extends Exception {
    public BookUnavailableException(String message) {
        super(message);
    }
}