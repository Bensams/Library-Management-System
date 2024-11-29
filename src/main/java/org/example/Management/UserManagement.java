// UserManagement.java
package org.example.Management;

import org.example.Accounts.Admin;
import org.example.Accounts.Borrower;
import org.example.Accounts.User;

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
        boolean userExists = users.removeIf(user -> user.getUserID().equals(userID));
        if (userExists) {
            serializedManagement.serializeUsers(users);
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