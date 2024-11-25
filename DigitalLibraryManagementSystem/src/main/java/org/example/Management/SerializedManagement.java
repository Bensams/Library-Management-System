package org.example.Management;

import org.example.Accounts.User;
import org.example.Library.Book;
import org.example.Library.BorrowedBook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializedManagement implements Serializable {
    private List<User> users;

    private static final long serialVersionUID = 1L;
    private static SerializedManagement instance;

    private File userFile = new File("src/main/resources/Data/UsersData.ser");
    private File bookFile = new File("src/main/resources/Data/BooksData.ser");
    private File borrowedBooksFile = new File("src/main/resources/Data/BorrowedBooks.ser");

    private SerializedManagement() {
        // Private constructor to prevent instantiation
        createFileIfNotExists(userFile);
        createFileIfNotExists(bookFile);
        createFileIfNotExists(borrowedBooksFile);
        users = deserializeUsers();
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

//    protected Object readResolve() {
//        return getInstance();
//    }

    private void createFileIfNotExists(File file) {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> deserializeUsers() {
        if (userFile.length() == 0) {
            users = new ArrayList<>(); // Initialize users to an empty list if file is empty
        } else {
            try (FileInputStream fileIn = new FileInputStream(userFile);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
                users = (List<User>) in.readObject();
            } catch (EOFException e) {
                System.err.println("Reached end of file unexpectedly: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public void serializeUsers(List<User> users) {
        try (FileOutputStream fileOut = new FileOutputStream(userFile);
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
        try (FileOutputStream fileOut = new FileOutputStream(bookFile);
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

        try (FileInputStream fileIn = new FileInputStream(bookFile);
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

    public void serializeAllBorrowedBooks(Map<User, List<BorrowedBook>> borrowedBooks) {
        try (FileOutputStream fileOut = new FileOutputStream(borrowedBooksFile);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(borrowedBooks);
            System.out.println("Borrowed books serialized successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<User, List<BorrowedBook>> deserializeAllBorrowedBooks() {
        Map<User, List<BorrowedBook>> allBorrowedBooks = new HashMap<>();

        if (borrowedBooksFile.length() == 0) {
            allBorrowedBooks = new HashMap<>(); // Initialize allBorrowedBooks to an empty map if file is empty
            return allBorrowedBooks;
        }

        try (FileInputStream fileIn = new FileInputStream(borrowedBooksFile);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            allBorrowedBooks = (Map<User, List<BorrowedBook>>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return allBorrowedBooks;
    }

    public void updateBook(Book book) {
        List<Book> books = deserializedBooks();
        books.removeIf(b -> b.getBookID().equals(book.getBookID()));
        books.add(book);
        serializedBooks(books);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        users.add(user);
        serializeUsers(users);
    }
}