package org.example.Accounts;

import org.example.Library.Book;
import org.example.Library.BorrowedBook;
import org.example.Management.SerializedManagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Borrower extends User implements BorrowerInterface, Serializable {
    private static final long serialVersionUID = 1L; // Unique ID for versioning
    SerializedManagement serializedManagement = SerializedManagement.getInstance();
    private List<BorrowedBook> borrowedBooks = new ArrayList<>();

    public Borrower(String userID, String name, String contactInfo, String username, String password) {
        super(userID, name, contactInfo,username, password,"Borrower");
        setBorrowedBooks(new ArrayList<>()); // Initialize the borrowedBooks list
    }

    @Override
    public void searchMyBorrowedBook(String query, String searchBy) {
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

//    @Override
//    public void borrowBook(Book borrowedBook) {
//        borrowedBooks.add(borrowedBook);
//    }
//
//    @Override
//    public void returnBook(Book borrowedBook) {
//        borrowedBooks.remove(borrowedBook);
//    }

    public List<BorrowedBook> getBorrowedBooks() {
        return borrowedBooks;
    }
    public void setBorrowedBooks(List<BorrowedBook> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
    // display borrowed books
//    public void displayBorrowedBooks() {
//        if (borrowedBooks.isEmpty()) {
//            System.out.println("No borrowed books.");
//        } else {
//            System.out.format("%-15s%-30s%-20s%-15s%-10s\n", "Book ID", "Title", "Author", "ISBN", "Availability");
//            for (BorrowedBook book : borrowedBooks) {
//                System.out.format("%-15s%-30s%-20s%-15s%-10d\n", book.getBookID(), book.getTitle(), book.getAuthor(), book.getISBN(), book.getQuantity());
//            }
//        }
//    }

}
