package org.example;
import org.example.Accounts.Admin;
import org.example.Accounts.User;
import org.example.Strategy.SearchStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Library {
    private SerializedManagement serializedManagement;
    private List<Book> books;
    private List<User> users;
    private SearchStrategy searchStrategy;

     // constructor
    public Library() {
        this.serializedManagement = SerializedManagement.getInstance();
        this.books = serializedManagement.deserializedBooks();
        this.users = serializedManagement.deserializeUsers();
    }

    public  void setSearchStrategy(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public List<Book> searchBooks(String query) {
        return searchStrategy.search(books, query);
    }

    // Existing methods...

    public void borrowBook() {
        // Implement borrow book logic
    }

    public void returnBook() {
        // Implement return book logic
    }

    public void addBookToLibrary() {
        // TODO: add book from book class functionality
    }

    public void removeBookFromLibrary() {
        // TODO: Remove Book From Library
    }

//    public Map<User, Book> trackBorrowedBooks() {
//        // TODO: Track Borrowed Books.
//    }

    public String generateSummary() {
        // TODO: Generate a Summary for available and borrowed Books.
        return "";
    }
}
