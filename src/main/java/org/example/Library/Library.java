package org.example.Library;
import org.example.Accounts.Borrower;
import org.example.Accounts.User;
import org.example.Management.SerializedManagement;
import org.example.Strategy.SearchStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Library {
    private static Library instance;
    private SerializedManagement serializedManagement;
    private List<Book> books;
    private List<User> users;
    private SearchStrategy searchStrategy;
    private Map<User, List<BorrowedBook>> allBorrowedBooks;
    private Borrower currentBorrower;

     // constructor
    public Library() {
        this.serializedManagement = SerializedManagement.getInstance();
        this.books = serializedManagement.deserializedBooks();
        this.users = serializedManagement.deserializeUsers();
        this.allBorrowedBooks = serializedManagement.deserializeAllBorrowedBooks();
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
            BorrowedBook borrowedBook = new BorrowedBook(currentBorrower.getUserID(), currentBorrower.getName(), book.getBookID(), book.getTitle(), book.getAuthor(), book.getISBN(), LocalDate.now());
            allBorrowedBooks.computeIfAbsent(currentBorrower, k -> new ArrayList<>()).add(borrowedBook);
            serializedManagement.serializedBooks(books);
            serializedManagement.serializeAllBorrowedBooks(allBorrowedBooks);
        }
    }

    public void returnBook(Book book) {
        List<BorrowedBook> borrowedBooks = allBorrowedBooks.get(currentBorrower);
        if (borrowedBooks != null) {
            BorrowedBook borrowedBook = borrowedBooks.stream()
                    .filter(b -> b.getBookID().equals(book.getBookID()))
                    .findFirst()
                    .orElse(null);
            if (borrowedBook != null) {
                book.setQuantity(book.getQuantity() + 1);
                borrowedBooks.remove(borrowedBook);
                serializedManagement.serializedBooks(books);
                serializedManagement.serializeAllBorrowedBooks(allBorrowedBooks);
            }
        }
    }

    public Map<User, List<BorrowedBook>> trackBorrowedBooks() {
        return allBorrowedBooks;
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

//    public void displayAllBorrowedBooks() {
//        System.out.format("%-15s%-20s%-12s%-20s%-15s%-20s\n", "User ID", "User Name", "Book ID", "Title", "Author", "ISBN");
//        System.out.println("------------------------------------------------------------------------------------------------------------------------");
//        for (Map.Entry<User, List<BorrowedBook>> entry : allBorrowedBooks.entrySet()) {
//            User user = entry.getKey();
//            List<Book> books = entry.getValue();
//            for (Book book : books) {
//                System.out.format("%-15s%-20s%-12s%-20s%-15s%-20s\n", user.getUserID(), user.getName(), book.getBookID(), book.getTitle(), book.getAuthor(), book.getISBN());
//            }
//        }
//    }

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
        if (allBorrowedBooks.isEmpty()) {
            summary.append("No borrowed books.\n");
        } else {
            for (Map.Entry<User, List<BorrowedBook>> entry : allBorrowedBooks.entrySet()) {
                User user = entry.getKey();
                List<BorrowedBook> userBooks = entry.getValue();
                summary.append("User: ").append(user.getName()).append("\n");
                for (BorrowedBook book : userBooks) {
                    summary.append(book.toString()).append("\n");
                }
            }
        }
        return summary.toString();
    }

    // Track borrowed books by a specific user
    public List<BorrowedBook> getBorrowedBooksByUser(User user) {
        return allBorrowedBooks.getOrDefault(user, new ArrayList<>());
    }

    // Display borrowed books by a specific user
//    public void displayBorrowedBooksByUser() {
//        List<BorrowedBook> borrowedBooksByUser = getBorrowedBooksByUser(currentBorrower);
//        if (borrowedBooksByUser.isEmpty()) {
//            System.out.println("No borrowed books for user: " + currentBorrower.getName());
//        } else {
//            System.out.format("%-15s%-30s%-20s%-15s%-10s\n", "Book ID", "Title", "Author", "ISBN", "Availability");
//            for (BorrowedBook book : borrowedBooksByUser) {
//                System.out.format("%-15s%-30s%-20s%-15s%-10d\n", book.getBookID(), book.getTitle(), book.getAuthor(), book.getISBN(), book.getQuantity());
//            }
//        }
//    }

    public boolean bookExists(String bookID) {
        for (Book book : books) {
            if (book.getBookID().equals(bookID)) {
                return true;
            }
        }
        return false;
    }

}
