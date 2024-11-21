package org.example;

import org.example.Accounts.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializedManagement implements Serializable {
    private static final long serialVersionUID = 1L;
    private static SerializedManagement instance;

    private File userFile = new File("UsersData.ser");
    private File bookFile = new File("BooksData.ser");

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

public void modifyBook(String bookID, String newTitle) {
    List<Book> books = deserializedBooks();
    if (books != null) {
        for (Book book : books) {
            if (book.getBookID().equals(bookID)) {
                book.setTitle(newTitle);
                break;
            }
        }
        serializedBooks(books);
    }
}

public void updateBook(Book book) {
    List<Book> books = deserializedBooks();
    for (int i = 0; i < books.size(); i++) {
        if (books.get(i).getBookID().equals(book.getBookID())) {
            books.set(i, book);
            break;
        }
    }
    serializedBooks(books);
}

}
