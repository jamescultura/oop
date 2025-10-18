// Transaction.java
public class Transaction {
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
    
    public String getTransactionId() { 
        return transactionId; 
    }
    
    public String getUserId() { 
        return userId; 
    }
    
    public String getBookId() { 
        return bookId; 
    }
    
    public String getDateBorrowed() { 
        return dateBorrowed; 
    }
    
    public String getDateReturned() { 
        return dateReturned; 
    }
    
    public void setDateReturned(String dateReturned) { 
        this.dateReturned = dateReturned; 
    }
    
    public String toFileString() {
        return transactionId + "," + userId + "," + bookId + "," + dateBorrowed + "," + dateReturned;
    }
}