package org.example.Accounts;

import org.example.SerializedManagement;

public interface AdminInterface
{
    public void addBook(String bookID, String title, String author, String ISBN, String publicationDate, int quantity);
    public void updateBook(String bookID, String newTitle, String newAuthor, String newISBN, String newPublicationDate, boolean newAvailability, int newQuantity);
    public void deleteBook(String bookID);
    public void viewReport();
    void viewRegisteredAccounts();
    void deleteRegisteredAccount(String userID);
}
