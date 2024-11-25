package org.example.Library;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class BorrowedBook {
    private SimpleStringProperty userID;
    private SimpleStringProperty userName;
    private SimpleStringProperty bookID;
    private SimpleStringProperty title;
    private SimpleStringProperty author;
    private SimpleStringProperty isbn;
    private SimpleStringProperty borrowedDate;

    public BorrowedBook(String userID, String userName, String bookID, String title, String author, String isbn, LocalDate borrowedDate) {
        this.userID = new SimpleStringProperty(userID);
        this.userName = new SimpleStringProperty(userName);
        this.bookID = new SimpleStringProperty(bookID);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.isbn = new SimpleStringProperty(isbn);
        this.borrowedDate = new SimpleStringProperty(borrowedDate.toString());
    }

    public String getUserID() {
        return userID.get();
    }

    public String getUserName() {
        return userName.get();
    }

    public String getBookID() {
        return bookID.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public String getISBN() {
        return isbn.get();
    }

    public String getBorrowedDate() {
        return borrowedDate.get();
    }
}