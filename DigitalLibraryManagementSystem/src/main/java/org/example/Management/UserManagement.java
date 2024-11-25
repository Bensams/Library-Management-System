// src/main/java/org/example/Management/UserManagement.java
package org.example.Management;

import javafx.scene.control.Label;
import org.example.Accounts.Admin;
import org.example.Accounts.Borrower;
import org.example.Accounts.User;

import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

public class UserManagement implements Serializable {
    private static final long serialVersionUID = 1L;
    private SerializedManagement serializedManagement;
    private Admin currentAdmin;

    public UserManagement(Admin currentAdmin) {
        this.serializedManagement = SerializedManagement.getInstance();
        this.currentAdmin = currentAdmin;
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

    public void registerUser(String userID, String name, String contactInfo, String username, String password, String role) {

        User newUser;
        if (role.equalsIgnoreCase("Admin")) {
            newUser = new Admin(userID, name, contactInfo, username , password);
        } else{
            newUser = new Borrower(userID, name, contactInfo, username, password);
        }
        List<User> users = serializedManagement.deserializeUsers();
        users.add(newUser);
        serializedManagement.serializeUsers(users); // Always serialize after adding a new user
    }

    public boolean userExists(String userID, String username) {
        List<User> users = serializedManagement.deserializeUsers();
        for (User user : users) {
            if (user.getUserID().equals(userID) || user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    // updateUser method
    public void updateUserInfo(String userID, String name, String contactInfo, String username, String password) {
        List<User> users = serializedManagement.deserializeUsers();
        for (User user : users) {
            if (user.getUserID().equals(userID)) {
                user.setName(name);
                user.setContactInfo(contactInfo);
                user.setUsername(username);
                user.setPassword(password);
                serializedManagement.serializeUsers(users); // Always serialize after updating user data
                System.out.println("User with ID " + userID + " has been successfully updated.");
                return;
            }
        }
        System.out.println("User with ID " + userID + " not found.");
    }

}