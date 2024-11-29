// Library.java
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

    public void setSearchStrategy(SearchStrategy searchStrategy) {
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

    public boolean borrowBook(Book book, Borrower borrower, LocalDate dueDate) {
        this.books = serializedManagement.deserializedBooks();

        if (borrower == null) {
            System.out.println("Borrower is not set.");
            return false;
        }

        Book bookToBorrow = books.stream()
                .filter(b -> b.getBookID().equals(book.getBookID()))
                .findFirst()
                .orElse(null);

        if (bookToBorrow != null && bookToBorrow.getQuantity() > 0) {
            bookToBorrow.setQuantity(bookToBorrow.getQuantity() - 1);
            BorrowedBook borrowedBook = new BorrowedBook(borrower.getUserID(), borrower.getName(), bookToBorrow.getBookID(), bookToBorrow.getTitle(), bookToBorrow.getAuthor(), bookToBorrow.getISBN(), LocalDate.now(), dueDate);

            if (borrower.getBorrowedBooks() == null) {
                borrower.setBorrowedBooks(new ArrayList<>()); // Initialize the list if it's null
            }

            allBorrowedBooks.computeIfAbsent(borrower, k -> new ArrayList<>()).add(borrowedBook);
            serializedManagement.updateBook(bookToBorrow);
            serializedManagement.serializeAllBorrowedBooks(allBorrowedBooks);
            borrower.getBorrowedBooks().add(borrowedBook);
            serializedManagement.serializeUsers(users);
            serializedManagement.serializeBorrowedBooksByUser(borrower, borrower.getBorrowedBooks()); // Serialize borrowed books for the user

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

    public void returnBook(BorrowedBook borrowedBook) {
        System.out.println("Attempting to return book: " + borrowedBook.getTitle());
        this.allBorrowedBooks = serializedManagement.deserializeAllBorrowedBooks();


        if (currentBorrower == null) {
            System.out.println("Current borrower is not set.");
            return;
        } else {
            currentBorrower.setBorrowedBooks(serializedManagement.deserializeBorrowedBooksByUser(currentBorrower));
        }

        List<BorrowedBook> borrowedBooks = currentBorrower.getBorrowedBooks();

        if (borrowedBooks != null) {
            BorrowedBook bookToReturn = borrowedBooks.stream()
                    .filter(b -> b.getBookID().equals(borrowedBook.getBookID()))
                    .findFirst()
                    .orElse(null);
            if (bookToReturn != null) {
                Book book = books.stream()
                        .filter(b -> b.getBookID().equals(borrowedBook.getBookID()))
                        .findFirst()
                        .orElse(null);

                if (book != null) {

                    currentBorrower.setBorrowedBooks(borrowedBooks);

                    book.setQuantity(book.getQuantity() + 1);
                    borrowedBooks.remove(bookToReturn);

                    allBorrowedBooks.put(currentBorrower, borrowedBooks); // Update the map
                    currentBorrower.getBorrowedBooks().remove(bookToReturn);
                    serializedManagement.updateBook(book);
                    serializedManagement.serializeAllBorrowedBooks(allBorrowedBooks);

                    serializedManagement.serializeUsers(users);
                    serializedManagement.serializeBorrowedBooksByUser(currentBorrower, currentBorrower.getBorrowedBooks());

                    System.out.println("Book successfully returned: " + book.getTitle());

                    if (manageBorrower != null) {
                        manageBorrower.refreshAllTable();
                    }
                } else {
                    System.out.println("Book not found.");
                }
            } else {
                System.out.println("Borrowed book not found in the borrower's list.");
            }
        } else {
            System.out.println("No borrowed books found for the current borrower.");
        }
    }

    public boolean isBookAlreadyBorrowedByUser(Book book, Borrower borrower) {
        List<BorrowedBook> borrowedBooks = getBorrowedBooksByUser(borrower);
        return borrowedBooks.stream().anyMatch(borrowedBook -> borrowedBook.getBookID().equals(book.getBookID()));
    }

    public List<Book> getAvailableBooks() {
        this.books = serializedManagement.deserializedBooks();
        return books.stream()
                .filter(book -> book.getQuantity() > 0)
                .collect(Collectors.toList());
    }

    public Map<User, List<BorrowedBook>> trackBorrowedBooks() {
        allBorrowedBooks = serializedManagement.deserializeAllBorrowedBooks();

        return allBorrowedBooks;
    }

    public List<BorrowedBook> getBorrowedBooksByUser(User user) {
        this.allBorrowedBooks = serializedManagement.deserializeAllBorrowedBooks();
        return allBorrowedBooks.getOrDefault(user, new ArrayList<>());
    }

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

    public List<User> getUsers() {
        this.users = serializedManagement.deserializeUsers();
        return users;
    }

    public Map<User, List<BorrowedBook>> getAllBorrowedBooks() {
        this.allBorrowedBooks = serializedManagement.deserializeAllBorrowedBooks();
        return allBorrowedBooks;
    }

}