// User.java - Subclass of Person
import java.util.ArrayList;

public class User extends Person {
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
    
    public String getPassword() { 
        return password; 
    }
    
    public String getRole() { 
        return role; 
    }
    
    public ArrayList<String> getBorrowedBooks() { 
        return borrowedBooks; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }
    
    public void setRole(String role) { 
        this.role = role; 
    }
    
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