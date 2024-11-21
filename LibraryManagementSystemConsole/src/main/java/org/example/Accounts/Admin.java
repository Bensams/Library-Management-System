package org.example.Accounts;

import org.example.Book;
import org.example.Library;
import org.example.SerializedManagement;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Admin extends User implements AdminInterface, Serializable {
    private static final long serialVersionUID = 1L; // Unique ID for versioning
    private SerializedManagement serializedManagement;

    public Admin(String userID, String name, String contactInfo, String password) {
        super(userID, name, contactInfo, password, "Admin");
        this.serializedManagement =  SerializedManagement.getInstance();
    }

    // Admin Interface Methods
    @Override
    public void addBook(String bookID, String title, String author, String ISBN, String publicationDate, int quantity) {
        Book book = new Book(bookID, title, author, ISBN,publicationDate, true, quantity);
        serializedManagement.serializedBook(book);
    }

    @Override
    public void updateBook(String bookID, String newTitle, String newAuthor, String newISBN, String newPublicationDate, boolean newAvailability, int newQuantity) {
        Book book = serializedManagement.deserializedBook(bookID);
        if (book != null) {
            book.setTitle(newTitle);
            book.setAuthor(newAuthor);
            book.setISBN(newISBN);
            book.setAvailability(newAvailability);
            book.setQuantity(newQuantity);
            serializedManagement.updateBook(book);
        }
    }

    @Override
    public void deleteBook(String bookID) {
        List<Book> books = serializedManagement.deserializedBooks();
        books.removeIf(book -> book.getBookID().equals(bookID));
        serializedManagement.serializedBooks(books);
    }

    @Override
    public void viewReport() {
        List<Book> books = serializedManagement.deserializedBooks();
        for (Book book : books) {
            System.out.println(book);
        }
    }

    @Override
    public void viewRegisteredAccounts() {
        List<User> users = serializedManagement.deserializeUsers();
        System.out.format("%-15s%-20s%-20s%-15s\n", "User ID", "Name", "Contact Info", "Role");
        System.out.println("---------------------------------------------------------------");
        for (User user : users) {
            System.out.format("%-15s%-20s%-20s%-15s\n", user.getUserID(), user.getName(), user.getContactInfo(), user.getRole());
        }
    }

    @Override
    public void deleteRegisteredAccount(String userID) {
        List<User> users = serializedManagement.deserializeUsers();
        users.removeIf(user -> user.getUserID().equals(userID));
        serializedManagement.serializeUsers(users);
    }

    // Admin Specific Methods
    public void registerUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter User ID: ");
        String userID = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Contact Info (Email/Phone No.): ");
        String contactInfo = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Role (Admin/Borrower): ");
        String role = scanner.nextLine();

        User newUser;
        if (role.equalsIgnoreCase("Admin")) {
            newUser = new Admin(userID, name, contactInfo, password);
        } else if (role.equalsIgnoreCase("Borrower")) {
            newUser = new Borrower(userID, name, contactInfo, password);
        } else {
            System.out.println("Invalid role. Please try again.");
            return;
        }
        List<User> users = serializedManagement.deserializeUsers();
        users.add(newUser);
        serializedManagement.serializeUsers(users); // Always serialize after adding a new user
        System.out.println("Signup successful! You can now login.");
    }

}
