// Book.java
public class Book {
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
    
    public String getBookId() { 
        return bookId; 
    }
    
    public String getTitle() { 
        return title; 
    }
    
    public String getAuthor() { 
        return author; 
    }
    
    public boolean isAvailable() { 
        return available; 
    }
    
    public void setTitle(String title) { 
        this.title = title; 
    }
    
    public void setAuthor(String author) { 
        this.author = author; 
    }
    
    public void setAvailable(boolean available) { 
        this.available = available; 
    }
    
    public String toFileString() {
        return bookId + "," + title + "," + author + "," + available;
    }
}