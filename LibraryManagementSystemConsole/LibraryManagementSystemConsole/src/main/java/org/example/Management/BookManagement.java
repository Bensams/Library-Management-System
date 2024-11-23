// src/main/java/org/example/Management/BookManagement.java
package org.example.Management;

import org.example.Book;
import org.example.Library;

import java.io.Serializable;
import java.util.List;

public class BookManagement implements Serializable {
    private static final long serialVersionUID = 1L;
    private SerializedManagement serializedManagement;

    public BookManagement() {
        this.serializedManagement = SerializedManagement.getInstance();
    }

    public void addBook(String bookID, String title, String author, String ISBN, String publicationDate, int quantity) {
        Book book = new Book(bookID, title, author, ISBN, publicationDate, true, quantity);
        Library.getInstance().addBookToLibrary(book);
    }

    public void updateBook(String bookID, String newTitle, String newAuthor, String newISBN, String newPublicationDate, boolean newAvailability, int newQuantity) {
        Book book = new Book(bookID, newTitle, newAuthor, newISBN, newPublicationDate, newAvailability, newQuantity);
        Library.getInstance().updateBook(book);
    }

    public void deleteBook(String bookID) {
        Library.getInstance().removeBookFromLibrary(bookID);
    }

    public void viewReport() {
        List<Book> books = serializedManagement.deserializedBooks();
        System.out.format("%-15s%-30s%-20s%-15s%-20s%-10s\n", "Book ID", "Title", "Author", "ISBN", "Publication Date", "Quantity");
        System.out.println("------------------------------------------------------------------------------------------------------------");
        for (Book book : books) {
            System.out.format("%-15s%-30s%-20s%-15s%-20s%-10d\n", book.getBookID(), book.getTitle(), book.getAuthor(), book.getISBN(), book.getPublicationDate(), book.getQuantity());
        }
    }
}