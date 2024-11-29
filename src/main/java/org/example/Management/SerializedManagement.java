// SerializedManagement.java
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
        List<User> users = new ArrayList<>();
        if (userFile.length() == 0) {
            return users;
        }
        try (FileInputStream fileIn = new FileInputStream(userFile);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            users = (List<User>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
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
            return books;
        }

        try (FileInputStream fileIn = new FileInputStream(bookFile);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            books = (List<Book>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
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
        return allBorrowedBooks;
    }
    try (FileInputStream fileIn = new FileInputStream(borrowedBooksFile);
         ObjectInputStream in = new ObjectInputStream(fileIn)) {
        Object obj = in.readObject();
        if (obj instanceof Map) {
            allBorrowedBooks = (Map<User, List<BorrowedBook>>) obj;
        } else {
            throw new IOException("Data format is incorrect.");
        }
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
    return allBorrowedBooks;
}

    public void serializeBorrowedBooksByUser(User user, List<BorrowedBook> borrowedBooks) {
        File userBorrowedBooksFile = new File("src/main/resources/Data/BorrowedBooks_" + user.getUserID() + ".ser");
        try (FileOutputStream fileOut = new FileOutputStream(userBorrowedBooksFile);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(borrowedBooks);
            System.out.println("Borrowed books for user " + user.getUserID() + " serialized successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<BorrowedBook> deserializeBorrowedBooksByUser(User user) {
        File userBorrowedBooksFile = new File("src/main/resources/Data/BorrowedBooks_" + user.getUserID() + ".ser");
        List<BorrowedBook> borrowedBooks = null;
        if (userBorrowedBooksFile.length() == 0) {
            return borrowedBooks;
        }
        try (FileInputStream fileIn = new FileInputStream(userBorrowedBooksFile);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            borrowedBooks = (List<BorrowedBook>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return borrowedBooks;
    }

    // New method to get borrowed books directly
    public List<BorrowedBook> getBorrowedBooks(User user) {
        return deserializeBorrowedBooksByUser(user);
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

    public void addUser(User user) {
        users.add(user);
        serializeUsers(users);
    }
}