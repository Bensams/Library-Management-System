package org.example;
import org.example.Accounts.Borrower;
import org.example.Accounts.User;
import org.example.Management.SerializedManagement;
import org.example.Strategy.SearchStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Library {
    private static Library instance;
    private SerializedManagement serializedManagement;
    private List<Book> books;
    private List<User> users;
    private SearchStrategy searchStrategy;
    private Map<User, List<Book>> borrowedBooks;
    private Borrower currentBorrower;

     // constructor
    public Library() {
        this.serializedManagement = SerializedManagement.getInstance();
        this.books = serializedManagement.deserializedBooks();
        this.users = serializedManagement.deserializeUsers();
        this.borrowedBooks = serializedManagement.deserializeBorrowedBooks();
    }

    public static synchronized Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    public void setCurrentBorrower(Borrower borrower) {
        this.currentBorrower = borrower;
    }

    public  void setSearchStrategy(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public List<Book> searchBooks(String query) {
        return searchStrategy.search(books, query);
    }

    public void searchResults(String query) {
        List<Book> searchResults = searchBooks(query);
        if (searchResults.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.format("%-15s%-30s%-20s%-15s%-20s%-10s\n", "Book ID", "Title", "Author", "ISBN", "Publication Date", "Availability");
            for (Book book : searchResults) {
                System.out.format("%-15s%-30s%-20s%-15s%-20s%-10d\n", book.getBookID(), book.getTitle(), book.getAuthor(), book.getISBN(), book.getPublicationDate(), book.getQuantity());
            }
        }
    }

    // Existing methods...

    public void borrowBook(Book book) {
        if (books.contains(book) && book.getQuantity() > 0) {
            book.setQuantity(book.getQuantity() - 1);
            borrowedBooks.computeIfAbsent(currentBorrower, k -> new ArrayList<>()).add(book);
            currentBorrower.getBorrowedBooks().add(book);
            serializedManagement.serializedBooks(books);
            serializedManagement.serializeBorrowedBooks(borrowedBooks);
        }
    }

    public void returnBook(Book book) {
        if (borrowedBooks.containsKey(currentBorrower) && borrowedBooks.get(currentBorrower).contains(book)) {
            book.setQuantity(book.getQuantity() + 1);
            borrowedBooks.get(currentBorrower).remove(book);
            currentBorrower.getBorrowedBooks().remove(book);
            serializedManagement.serializedBooks(books);
            serializedManagement.serializeBorrowedBooks(borrowedBooks);
        }
    }

    // getBookbyID
    public Book getBookById(String bookID) {
        for (Book book : books) {
            if (book.getBookID().equals(bookID)) {
                return book;
            }
        }
        return null;
    }

    public void addBookToLibrary(Book book) {
        books.add(book);
        serializedManagement.serializedBooks(books);
        System.out.println("Book added successfully!");
    }

    public void updateBook(Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookID().equals(updatedBook.getBookID())) {
                books.set(i, updatedBook);
                serializedManagement.serializedBooks(books);
                System.out.println("Book updated successfully!");
                return;
            }
        }
        System.out.println("Book not found!");
    }

    public void removeBookFromLibrary(String bookID) {
        boolean removed = books.removeIf(book -> book.getBookID().equals(bookID));
        if (removed) {
            serializedManagement.serializedBooks(books);
            System.out.println("Book removed successfully!");
        } else {
            System.out.println("Book not found!");
        }
    }

    public Map<User, List<Book>> trackBorrowedBooks() {
        return borrowedBooks;
    }

    public void displayBorrowedBooks() {
        System.out.format("%-15s%-20s%-12s%-20s%-15s%-20s\n", "User ID", "User Name", "Book ID", "Title", "Author", "ISBN");
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        for (Map.Entry<User, List<Book>> entry : borrowedBooks.entrySet()) {
            User user = entry.getKey();
            List<Book> books = entry.getValue();
            for (Book book : books) {
                System.out.format("%-15s%-20s%-12s%-20s%-15s%-20s\n", user.getUserID(), user.getName(), book.getBookID(), book.getTitle(), book.getAuthor(), book.getISBN());
            }
        }
    }

    public String generateSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Available Books:\n");
        if (books.isEmpty()) {
            summary.append("No available books.\n");
        } else {
            for (Book book : books) {
                if (book.getQuantity() == 0) {
                    continue;
                } else {
                    summary.append(book.toString()).append("\n");
                }
            }
        }
        summary.append("\nBorrowed Books:\n");
        if (borrowedBooks.isEmpty()) {
            summary.append("No borrowed books.\n");
        } else {
            for (Map.Entry<User, List<Book>> entry : borrowedBooks.entrySet()) {
                User user = entry.getKey();
                List<Book> userBooks = entry.getValue();
                summary.append("User: ").append(user.getName()).append("\n");
                for (Book book : userBooks) {
                    summary.append(book.toString()).append("\n");
                }
            }
        }
        return summary.toString();
    }

    // Track borrowed books by a specific user
    public List<Book> getBorrowedBooksByUser(User user) {
        return borrowedBooks.getOrDefault(user, new ArrayList<>());
    }

    // Display borrowed books by a specific user
    public void displayBorrowedBooksByUser() {
        List<Book> borrowedBooksByUser = getBorrowedBooksByUser(currentBorrower);
        if (borrowedBooksByUser.isEmpty()) {
            System.out.println("No borrowed books for user: " + currentBorrower.getName());
        } else {
            System.out.format("%-15s%-30s%-20s%-15s%-10s\n", "Book ID", "Title", "Author", "ISBN", "Availability");
            for (Book book : borrowedBooksByUser) {
                System.out.format("%-15s%-30s%-20s%-15s%-10d\n", book.getBookID(), book.getTitle(), book.getAuthor(), book.getISBN(), book.getQuantity());
            }
        }
    }

}
