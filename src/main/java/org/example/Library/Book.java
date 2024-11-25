package org.example.Library;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L; // Unique ID for versioning

    private String bookID;
    private String title;
    private String author;
    private String ISBN;
    private String publicationDate;
    private boolean availability;
    private int quantity;

    // Constructor
    public Book(String bookID, String title, String author, String ISBN, String publicationDate, boolean availability, int quantity) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.publicationDate = publicationDate;
        this.availability = availability;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("Book ID: %s, Title: %s, Author: %s, ISBN: %s, Publication Date: %s, Quantity: %d",
                bookID, title, author, ISBN, publicationDate, quantity);
    }

    // Getters and Setters

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        if (quantity == 0) {
            this.availability = false;
        }
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

}