// src/main/java/org/example/Management/SerializedManagement.java
package org.example.Management;

import org.example.Accounts.User;
import org.example.Book;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializedManagement implements Serializable {
    private static final long serialVersionUID = 1L;
    private static SerializedManagement instance;

    private File userFile = new File("UsersData.ser");
    private File bookFile = new File("BooksData.ser");
    private File borrowedBooksFile = new File("BorrowedBooks.ser");

    private SerializedManagement() {
        // Private constructor to prevent instantiation
    }

    public static SerializedManagement getInstance() {
        if (instance == null) {
            synchronized (SerializedManagement.class) {
                if (instance == null) {
                    instance = new SerializedManagement();
                }
            }
        }
        return instance;
    }

    protected Object readResolve() {
        return getInstance();
    }

    public List<User> deserializeUsers() {
        List<User> users = new ArrayList<>();
        try (FileInputStream fileIn = new FileInputStream("UsersData.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            users = (List<User>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public void serializeUsers(List<User> users) {
        try (FileOutputStream fileOut = new FileOutputStream("UsersData.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(users);
            System.out.println("Serialized data is saved in UsersData.ser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void modifyUser(String userID, String newName) {
        List<User> users = deserializeUsers();
        if (users != null) {
            for (User user : users) {
                if (user.getUserID().equals(userID)) {
                    user.setName(newName);
                    break;
                }
            }
            serializeUsers(users);
        }
    }

    public void serializedBooks(List<Book> books) {
        try (FileOutputStream fileOut = new FileOutputStream("BooksData.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(books);
            System.out.println("Books serialized successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Book> deserializedBooks() {
        List<Book> books = new ArrayList<>();

        if (bookFile.length() == 0) {
            return books; // Return empty list if file is empty
        }

        try (FileInputStream fileIn = new FileInputStream("BooksData.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            books = (List<Book>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }

    public void serializedBook(Book book) {
        List<Book> books = deserializedBooks();
        books.add(book);
        serializedBooks(books);
    }

    public Book deserializedBook(String bookID) {
        List<Book> books = deserializedBooks();
        Book book = null;

        for (Book b : books) {
            if (b.getBookID().equals(bookID)) {
                book = b;
                break;
            }
        }

        return book;
    }

    public void serializeBorrowedBooks(Map<User, List<Book>> borrowedBooks) {
        try (FileOutputStream fileOut = new FileOutputStream(borrowedBooksFile);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(borrowedBooks);
            System.out.println("Borrowed books serialized successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<User, List<Book>> deserializeBorrowedBooks() {
        Map<User, List<Book>> borrowedBooks = new HashMap<>();

        if (borrowedBooksFile.length() == 0) {
            return borrowedBooks; // Return empty map if file is empty
        }

        try (FileInputStream fileIn = new FileInputStream(borrowedBooksFile);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            borrowedBooks = (Map<User, List<Book>>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return borrowedBooks;
    }

    public void updateBook(Book book) {
        List<Book> books = deserializedBooks();
        books.removeIf(b -> b.getBookID().equals(book.getBookID()));
        books.add(book);
        serializedBooks(books);
    }
}