// UserManagement.java
package org.example.Management;

import org.example.Accounts.Admin;
import org.example.Accounts.Borrower;
import org.example.Accounts.User;
import org.example.Library.Book;
import org.example.Library.BorrowedBook;
import org.example.Library.Library;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class UserManagement implements Serializable {
    private static final long serialVersionUID = 1L;
    private SerializedManagement serializedManagement;

    public UserManagement() {
        this.serializedManagement = SerializedManagement.getInstance();
    }

    public void registerUser(String userID, String name, String contactInfo, String username, String password, String role) {
        List<User> users = serializedManagement.deserializeUsers();
        User newUser;
        if ("Admin".equalsIgnoreCase(role)) {
            newUser = new Admin(userID, name, contactInfo, username, password);
        } else {
            newUser = new Borrower(userID, name, contactInfo, username, password);
        }
        users.add(newUser);
        serializedManagement.serializeUsers(users);
        System.out.println("User registered successfully: " + name);
    }

    public void deleteRegisteredAccount(String userID) {
    List<User> users = serializedManagement.deserializeUsers();
    Library library = Library.getInstance();
    User userToDelete = users.stream()
            .filter(user -> user.getUserID().equals(userID))
            .findFirst()
            .orElse(null);

    if (userToDelete != null) {
        // Fetch the borrowed books of the user
        List<BorrowedBook> borrowedBooks = library.getBorrowedBooksByUser(userToDelete);

        // Return each borrowed book
        for (BorrowedBook borrowedBook : borrowedBooks) {
            Book book = library.getBookById(borrowedBook.getBookID());
            if (book != null) {
                library.returnBook(borrowedBook);
            }
        }

        // Remove the user from the allBorrowedBooks map
        library.getAllBorrowedBooks().remove(userToDelete);

        // Delete the user
        users.remove(userToDelete);
        serializedManagement.serializeUsers(users);

        // Delete the serialized file for the user
        File userBorrowedBooksFile = new File("src/main/resources/Data/BorrowedBooks_" + userID + ".ser");
        if (userBorrowedBooksFile.exists()) {
            if (userBorrowedBooksFile.delete()) {
                System.out.println("Serialized file for user " + userID + " deleted successfully.");
            } else {
                System.out.println("Failed to delete serialized file for user " + userID + ".");
            }
        }

        System.out.println("User with ID " + userID + " has been successfully deleted.");
    } else {
        System.out.println("User with ID " + userID + " not found.");
    }
}

    public boolean userExists(String userID, String username) {
        List<User> users = serializedManagement.deserializeUsers();
        return users.stream().anyMatch(user -> user.getUserID().equals(userID) || user.getUsername().equals(username));
    }

    public List<User> getAllUsers() {
        return serializedManagement.deserializeUsers();
    }

    public void updateUserInfo(String userID, String name, String contactInfo, String username, String password) {
        List<User> users = serializedManagement.deserializeUsers();
        for (User user : users) {
            if (user.getUserID().equals(userID)) {
                user.setName(name);
                user.setContactInfo(contactInfo);
                user.setUsername(username);
                user.setPassword(password);
                break;
            }
        }
        serializedManagement.serializeUsers(users);
    }
}