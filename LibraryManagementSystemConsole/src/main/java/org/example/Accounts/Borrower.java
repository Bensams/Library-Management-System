package org.example.Accounts;

import org.example.Book;
import org.example.SerializedManagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Borrower extends User implements BorrowerInterface, Serializable {
    private static final long serialVersionUID = 1L; // Unique ID for versioning
    SerializedManagement serializedManagement = SerializedManagement.getInstance();
    private List<Book> borrowedBooks = new ArrayList<>();

    public Borrower(String userID, String name, String contactInfo, String password) {
        super(userID, name, contactInfo, password, "Borrower");
    }

    @Override
    public void searchBook(String query, String searchBy) {
        List<Book> books = serializedManagement.deserializedBooks();
        List<Book> result;

        switch (searchBy.toLowerCase()) {
            case "title":
                result = books.stream()
                        .filter(book -> book.getTitle().equalsIgnoreCase(query))
                        .collect(Collectors.toList());
                break;
            case "author":
                result = books.stream()
                        .filter(book -> book.getAuthor().equalsIgnoreCase(query))
                        .collect(Collectors.toList());
                break;
            case "isbn":
                result = books.stream()
                        .filter(book -> book.getISBN().equalsIgnoreCase(query))
                        .collect(Collectors.toList());
                break;
            default:
                result = null;
                System.out.println("Invalid search criteria.");
        }

        if (result != null && !result.isEmpty()) {
            result.forEach(System.out::println);
        } else {
            System.out.println("No books found.");
        }
    }

    @Override
    public void borrowBook(String bookID) {
        Book book = serializedManagement.deserializedBook(bookID);
        if (book != null && book.isAvailable() && book.getQuantity() > 0) {
            book.setQuantity(book.getQuantity() - 1);
            if (book.getQuantity() == 0) {
                book.setAvailability(false);
            }
            serializedManagement.updateBook(book);
            borrowedBooks.add(book);
            System.out.println("Book borrowed successfully.");
        } else {
            System.out.println("Book is not available.");
        }
    }

    @Override
    public void returnBook(String bookID) {
        Book book = serializedManagement.deserializedBook(bookID);
        if (book != null && borrowedBooks.contains(book)) {
            book.setQuantity(book.getQuantity() + 1);
            book.setAvailability(true);
            serializedManagement.updateBook(book);
            borrowedBooks.remove(book);
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("Book not found in borrowed books.");
        }
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
}
