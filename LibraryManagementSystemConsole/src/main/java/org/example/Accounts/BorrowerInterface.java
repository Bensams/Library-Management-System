// src/main/java/org/example/Accounts/BorrowerInterface.java
package org.example.Accounts;

public interface BorrowerInterface {
    void searchBook(String query, String searchBy);
    void borrowBook(String bookID);
    void returnBook(String bookID);
}