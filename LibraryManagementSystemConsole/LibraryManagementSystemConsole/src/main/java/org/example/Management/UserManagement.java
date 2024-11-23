// src/main/java/org/example/Management/UserManagement.java
package org.example.Management;

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

    public void viewRegisteredAccounts() {
        List<User> users = serializedManagement.deserializeUsers();
        if (currentAdmin.getName().equals("admin")) {
            System.out.format("%-15s%-20s%-20s%-15s%-20s\n", "User ID", "Name", "Contact Info", "Role", "Password");
            System.out.println("---------------------------------------------------------------");
            for (User user : users) {
                System.out.format("%-15s%-20s%-20s%-15s%-20s\n", user.getUserID(), user.getName(), user.getContactInfo(), user.getRole(), user.getPassword());
            }
        } else {
            System.out.format("%-15s%-20s%-20s%-15s\n", "User ID", "Name", "Contact Info", "Role");
            System.out.println("---------------------------------------------------------------");
            for (User user : users) {
                System.out.format("%-15s%-20s%-20s%-15s\n", user.getUserID(), user.getName(), user.getContactInfo(), user.getRole());
            }
            return;
        }

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