# Library Management System

A comprehensive Java-based Library Management System with user authentication, book borrowing/returning functionality, and admin management features.

## Project Structure

```
library-management-system/
│
├── Main.java                          # Application entry point
├── Person.java                        # Base class for users
├── User.java                          # User class (extends Person)
├── Book.java                          # Book entity class
├── Transaction.java                   # Transaction record class
├── InvalidOperationException.java     # Custom exception class
├── LibrarySystem.java                 # Main controller/system class
│
├── users.txt                          # User data storage
├── books.txt                          # Book data storage
└── transactions.txt                   # Transaction logs
```

## Features

### User Features
- Login with username and password (max 3 attempts)
- View all available books
- Borrow books (maximum 3 books per user)
- Return borrowed books
- Search books by title or author

### Admin Features
- All user features
- **User Management**: Add, update, delete, and display users
- **Catalogue Management**: Add, update, delete, and display books
- **Transaction Management**: View all transactions, filter by user or book

## Default Credentials

### Users
- **Username**: John Doe, **Password**: pass123, **Role**: user
- **Username**: Jane Smith, **Password**: abc123, **Role**: user

### Admin
- **Username**: Admin, **Password**: admin123, **Role**: admin

## How to Compile and Run

### Option 1: Using Command Line

1. **Compile all Java files:**
   ```bash
   javac Main.java Person.java User.java Book.java Transaction.java InvalidOperationException.java LibrarySystem.java
   ```

2. **Run the application:**
   ```bash
   java Main
   ```

### Option 2: Using VS Code or IDE

1. Open the project folder in your IDE
2. Make sure all `.java` files are in the same directory
3. Run `Main.java`

## File Formats

### users.txt
```
UserID,Name,Password,Role
U001,John Doe,pass123,user
A001,Admin,admin123,admin
```

### books.txt
```
BookID,Title,Author,Available
B001,The Great Gatsby,F. Scott Fitzgerald,true
B002,To Kill a Mockingbird,Harper Lee,true
```

### transactions.txt
```
TransactionID,UserID,BookID,DateBorrowed,DateReturned
T001,U001,B002,2025-10-14,null
T002,U002,B003,2025-10-10,2025-10-13
```

## OOP Concepts Implemented

### 1. Inheritance
- `User` class extends `Person` base class

### 2. Polymorphism
- `displayInfo()` method is overridden in `User` class
- Demonstrated in `addUser()` method:
  ```java
  Person p = newUser;
  p.displayInfo();
  ```

### 3. Encapsulation
- Private fields with public getters/setters
- Protected fields in base class

### 4. Exception Handling
- `FileNotFoundException` - when text files are missing
- `IOException` - for file read/write errors
- `NullPointerException` - for missing data
- `InvalidOperationException` - custom exception for invalid operations

## Bonus Features Implemented

1. ✅ **Search Functionality**: Search books by title or author
2. ✅ **Auto-generate Transaction IDs**: Automatically generates T001, T002, etc.
3. ✅ **Borrowing Limit**: Prevents borrowing more than 3 books at once

## System Requirements

- Java Development Kit (JDK) 8 or higher
- Any text editor or IDE (VS Code, Eclipse, IntelliJ IDEA, etc.)

## Notes

- All data is automatically saved when exiting the system
- If text files don't exist, the system creates them with default data
- Books can only be deleted if they are not currently borrowed
- Users can only be deleted if they have no unreturned books
- Admin cannot delete their own account

## Error Handling

The system handles various errors gracefully:
- Invalid login attempts (max 3)
- File not found (creates default files)
- Borrowing unavailable books
- Exceeding borrow limit
- Invalid user input

## Author

Created as a programming activity for Library Management System with User Login

## License

This project is created for educational purposes.