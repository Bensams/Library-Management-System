// src/main/java/org/example/Accounts/Admin.java
package org.example.Accounts;

import org.example.Management.BookManagement;
import org.example.Management.UserManagement;

import java.io.Serializable;

public class Admin extends User implements AdminInterface, Serializable {
    private static final long serialVersionUID = 1L;
    private BookManagement bookManagement;
    private UserManagement userManagement;

    public Admin(String userID, String name, String contactInfo, String password) {
        super(userID, name, contactInfo, password, "Admin");
        this.bookManagement = new BookManagement();
        this.userManagement = new UserManagement(this);
    }

    // Admin BookManagement Methods
    @Override
    public void addBook(String bookID, String title, String author, String ISBN, String publicationDate, int quantity) {
        bookManagement.addBook(bookID, title, author, ISBN, publicationDate, quantity);
    }

    @Override
    public void updateBook(String bookID, String newTitle, String newAuthor, String newISBN, String newPublicationDate, boolean newAvailability, int newQuantity) {
        bookManagement.updateBook(bookID, newTitle, newAuthor, newISBN, newPublicationDate, newAvailability, newQuantity);
    }

    @Override
    public void deleteBook(String bookID) {
        bookManagement.deleteBook(bookID);
    }

    @Override
    public void viewReport() {
        bookManagement.viewReport();
    }

    // Admin User Management Methods
    @Override
    public void viewRegisteredAccounts() {
        userManagement.viewRegisteredAccounts();
    }

    @Override
    public void deleteRegisteredAccount(String userID) {
        userManagement.deleteRegisteredAccount(userID);
    }

    @Override
    public void registerUser() {
        userManagement.registerUser();
    }
}