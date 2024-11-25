package org.example.Library;
import org.example.Accounts.Borrower;
import org.example.Accounts.User;
import org.example.Management.SerializedManagement;
import org.example.Strategy.SearchStrategy;
import org.example.digitallibrarymanagementsystem.ManageBorrower;

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
    private ManageBorrower manageBorrower;

    public void setManageBorrower(ManageBorrower manageBorrower) {
        this.manageBorrower = manageBorrower;
    }

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
        this.allBorrowedBooks = serializedManagement.deserializeAllBorrowedBooks();
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

    public boolean borrowBook(Book book) {
        this.books = serializedManagement.deserializedBooks();

        if (currentBorrower == null) {
            System.out.println("Current borrower is not set.");
            return false;
        }

        Book bookToBorrow = books.stream()
                .filter(b -> b.getBookID().equals(book.getBookID()))
                .findFirst()
                .orElse(null);

        if (bookToBorrow != null && bookToBorrow.getQuantity() > 0) {
            bookToBorrow.setQuantity(bookToBorrow.getQuantity() - 1);
            BorrowedBook borrowedBook = new BorrowedBook(currentBorrower.getUserID(), currentBorrower.getName(), bookToBorrow.getBookID(), bookToBorrow.getTitle(), bookToBorrow.getAuthor(), bookToBorrow.getISBN(), LocalDate.now());
            allBorrowedBooks.computeIfAbsent(currentBorrower, k -> new ArrayList<>()).add(borrowedBook);
            serializedManagement.updateBook(bookToBorrow);
            serializedManagement.serializeAllBorrowedBooks(allBorrowedBooks);
            currentBorrower.getBorrowedBooks().add(borrowedBook);

            System.out.println("Book successfully borrowed: " + bookToBorrow.getTitle());

            if (manageBorrower != null) {
                manageBorrower.refreshAllTable();
            }

            this.books = serializedManagement.deserializedBooks();
            this.allBorrowedBooks = serializedManagement.deserializeAllBorrowedBooks();

            return true;
        } else {
            System.out.println("Book not available or out of stock.");
            return false;
        }
    }

    public void returnBook(Book book) {
        this.allBorrowedBooks = serializedManagement.deserializeAllBorrowedBooks();

        List<BorrowedBook> borrowedBooks = allBorrowedBooks.get(currentBorrower);
        if (borrowedBooks != null) {
            BorrowedBook borrowedBook = borrowedBooks.stream()
                    .filter(b -> b.getBookID().equals(book.getBookID()))
                    .findFirst()
                    .orElse(null);
            if (borrowedBook != null) {
                Book bookToReturn = books.stream()
                        .filter(b -> b.getBookID().equals(book.getBookID()))
                        .findFirst()
                        .orElse(null);

                if (bookToReturn != null) {
                    bookToReturn.setQuantity(bookToReturn.getQuantity() + 1);
                    borrowedBooks.remove(borrowedBook);
                    currentBorrower.getBorrowedBooks().remove(borrowedBook);
                    serializedManagement.updateBook(bookToReturn);
                    serializedManagement.serializeAllBorrowedBooks(allBorrowedBooks);

                    System.out.println("Book successfully returned: " + bookToReturn.getTitle());
                    this.books = serializedManagement.deserializedBooks();
                    this.allBorrowedBooks = serializedManagement.deserializeAllBorrowedBooks();

                    if (manageBorrower != null) {
                        manageBorrower.refreshAllTable();
                    }
                }
            }
        }
    }

    public List<Book> getAvailableBooks() {
        this.books = serializedManagement.deserializedBooks();
        return books.stream()
                .filter(book -> book.getQuantity() > 0)
                .collect(Collectors.toList());
    }

    public Map<User, List<BorrowedBook>> trackBorrowedBooks() {
        return allBorrowedBooks;
    }

    // Track borrowed books by a specific user
    public List<BorrowedBook> getBorrowedBooksByUser(User user) {
        this.allBorrowedBooks = serializedManagement.deserializeAllBorrowedBooks();
        return allBorrowedBooks.getOrDefault(user, new ArrayList<>());
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


    public boolean bookExists(String bookID) {
        for (Book book : books) {
            if (book.getBookID().equals(bookID)) {
                return true;
            }
        }
        return false;
    }

}
