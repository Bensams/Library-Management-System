// src/main/java/org/example/Main.java
package org.example;

import org.example.Accounts.Admin;
import org.example.Accounts.Authentication;
import org.example.Accounts.User;
import org.example.Accounts.Borrower;
import org.example.Management.SerializedManagement;
import org.example.Strategy.AuthorSearchStrategy;
import org.example.Strategy.ISBNSearchStrategy;
import org.example.Strategy.TitleSearchStrategy;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SerializedManagement serializedManagement = SerializedManagement.getInstance();
        List<User> users = serializedManagement.deserializeUsers();

        if (users.isEmpty()) {
            Admin admin = new Admin("adminID", "admin", "admin", "admin");
            users.add(admin);

            Borrower borrower = new Borrower("borrowerID", "borrower", "borrower", "borrower");
            users.add(borrower);

            serializedManagement.serializeUsers(users);
        }

        Authentication auth = new Authentication(users);
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Library Management System");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        User user = auth.login();
                        if (user != null) {
                            if (user instanceof Admin) {
                                manageLibrary((Admin) user);
                            } else {
                                manageBorrower((Borrower) user);
                            }
                        }
                        break;
                    case 2:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        scanner.close();
    }

    private static void manageLibrary(Admin admin) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Library Management System - Admin");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Remove Book");
            System.out.println("4. View Report");
            System.out.println("5. Register User");
            System.out.println("6. View Registered Accounts");
            System.out.println("7. Delete Registered Account");
            System.out.println("8. Logout");

            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                //scanner.nextLine();

                switch (choice) {
                    case 1:
                        scanner.nextLine();
                        System.out.print("Enter book ID: ");
                        String bookID = scanner.nextLine();
                        System.out.print("Enter title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter author: ");
                        String author = scanner.nextLine();
                        System.out.print("Enter ISBN: ");
                        String ISBN = scanner.nextLine();
                        System.out.print("Enter publication date: ");
                        String publicationDate = scanner.nextLine();
                        System.out.print("Enter quantity: ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();
                        admin.addBook(bookID, title, author, ISBN, publicationDate, quantity);
                        break;
                    case 2:
                        System.out.print("Enter book ID to update: ");
                        String updateBookID = scanner.nextLine();
                        System.out.print("Enter new title: ");
                        String newTitle = scanner.nextLine();
                        System.out.print("Enter new author: ");
                        String newAuthor = scanner.nextLine();
                        System.out.print("Enter new ISBN: ");
                        String newISBN = scanner.nextLine();
                        System.out.print("Enter new publication date: ");
                        String newPublicationDate = scanner.nextLine();
                        System.out.print("Enter new availability (true/false): ");
                        boolean newAvailability = scanner.nextBoolean();
                        System.out.print("Enter new quantity: ");
                        int newQuantity = scanner.nextInt();
                        scanner.nextLine();
                        admin.updateBook(updateBookID, newTitle, newAuthor, newISBN, newPublicationDate, newAvailability, newQuantity);
                        break;
                    case 3:
                        System.out.print("Enter book ID to remove: ");
                        String deleteBookID = scanner.nextLine();
                        admin.deleteBook(deleteBookID);
                        break;
                    case 4:
                        admin.viewReport();
                        break;
                    case 5:
                        admin.registerUser();
                        break;
                    case 6:
                        admin.viewRegisteredAccounts();
                        break;
                    case 7:
                        scanner.nextLine();
                        System.out.print("Enter User ID to delete: ");
                        String userID = scanner.nextLine();
                        admin.deleteRegisteredAccount(userID);
                        break;
                    case 8:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                if (scanner.hasNextLine()) {
                    scanner.nextLine(); // Clear the invalid input
                }
            }
        }
    }

    private static void manageBorrower(Borrower borrower) {
        Library library = Library.getInstance();
        library.setCurrentBorrower(borrower);
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Library Management System - Borrower");
            System.out.println("1. Search Book");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            System.out.println("4. View My Borrowed Books");
            System.out.println("5. View All Borrowed Books");
            System.out.println("6. Generate Summary Report");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter search query: ");
                        String query = scanner.nextLine();
                        System.out.print("Search by (title/author/isbn): ");
                        String searchBy = scanner.nextLine();

                        switch (searchBy.toLowerCase()) {
                            case "title":
                                library.setSearchStrategy(new TitleSearchStrategy());
                                break;
                            case "author":
                                library.setSearchStrategy(new AuthorSearchStrategy());
                                break;
                            case "isbn":
                                library.setSearchStrategy(new ISBNSearchStrategy());
                                break;
                            default:
                                System.out.println("Invalid search criteria.");
                                continue;
                        }

                        library.searchResults(query);

                        break;
                    case 2:
                        System.out.print("Enter book ID to borrow: ");
                        String borrowBookID = scanner.nextLine();
                        Book bookToBorrow = library.getBookById(borrowBookID);
                        if (bookToBorrow != null) {
                            library.borrowBook(bookToBorrow);
                        } else {
                            System.out.println("Book not found.");
                        }
                        break;
                    case 3:
                        System.out.print("Enter book ID to return: ");
                        String returnBookID = scanner.nextLine();
                        Book bookToReturn = library.getBookById(returnBookID);
                        if (bookToReturn != null) {
                            library.returnBook(bookToReturn);
                            System.out.println("Book has successfully returned!.");
                        } else {
                            System.out.println("Book not found.");
                        }
                        break;
                    case 4:
                        library.displayBorrowedBooksByUser();
                        break;
                    case 5:
                        library.displayBorrowedBooks();
                        break;
                    case 6:
                        System.out.println(library.generateSummary());
                        break;
                    case 7:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                if (scanner.hasNextLine()) {
                    scanner.nextLine(); // Clear the invalid input
                }
            }
        }
    }
}