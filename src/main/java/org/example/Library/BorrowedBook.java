// BorrowedBook.java
package org.example.Library;

import java.io.Serializable;
import java.time.LocalDate;

public class BorrowedBook implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userID;
    private String userName;
    private String bookID;
    private String title;
    private String author;
    private String isbn;
    private String borrowedDate;
    private String dueDate; // New attribute

    public BorrowedBook(String userID, String userName, String bookID, String title, String author, String isbn, LocalDate borrowedDate, LocalDate dueDate) {
        this.userID = userID;
        this.userName = userName;
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.borrowedDate = borrowedDate.toString();
        this.dueDate = dueDate.toString(); // Initialize new attribute
    }

    // Getters and setters
    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getBookID() {
        return bookID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getISBN() {
        return isbn;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public String getDueDate() {
        return dueDate; // New getter
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate; // New setter
    }
}