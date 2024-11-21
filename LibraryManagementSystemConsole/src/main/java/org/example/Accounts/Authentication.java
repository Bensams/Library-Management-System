package org.example.Accounts;

import org.example.SerializedManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Authentication {
    SerializedManagement serializedManagement = SerializedManagement.getInstance();
    private final List<User> users;
    Scanner scanner = new Scanner(System.in);

    public Authentication(List<User> users) {
        this.users = users;
    }

    public void forgotPassword() {
        System.out.print("Enter User ID or Contact Info: ");
        String userIDOrContactInfo = scanner.nextLine();

        for (User user : users) {
            if (user.getUserID().equals(userIDOrContactInfo) || user.getContactInfo().equals(userIDOrContactInfo)) {
                System.out.print("Enter New Password: ");
                String newPassword = scanner.nextLine();
                user.setPassword(newPassword);
                serializedManagement.serializeUsers(users); // Always serialize after updating user data
                System.out.println("Password reset successful! You can now login with your new password.");
                return;
            }
        }
        System.out.println("User not found. Please try again.");
    }

    public User login() {
        System.out.print("Enter User ID or Contact Info: ");
        String userIDOrContactInfo = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if ((user.getUserID().equals(userIDOrContactInfo) || user.getContactInfo().equals(userIDOrContactInfo))
                    && user.getPassword().equals(password)) {
                System.out.println("Login successful!");
                System.out.println("Welcome, " + user.getRole() + "!");

                return user;
            }
        }
        System.out.println("Invalid credentials. Please try again.");
        System.out.print("Forgot Password? (yes/no): ");
        String forgotPassword = scanner.nextLine();
        if (forgotPassword.equalsIgnoreCase("yes")) {
            forgotPassword();
        }
        return null;
    }


}
