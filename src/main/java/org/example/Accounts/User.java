package org.example.Accounts;

import org.example.Library.BorrowedBook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userID;
    private String name;
    private String contactInfo;
    private String username;
    private String password;
    private String role;
    private List<BorrowedBook> borrowedBooks;

    // Constructor
    public User(String userID, String name, String contactInfo, String username, String password, String role) {
        this.userID = userID;
        this.name = name;
        this.contactInfo = contactInfo;
        this.username = username;
        this.password = password;
        this.role = role;
        this.borrowedBooks = new ArrayList<>();
    }

    // Getters and setters
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<BorrowedBook> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<BorrowedBook> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }


}