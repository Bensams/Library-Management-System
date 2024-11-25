package org.example.Accounts;

public interface AdminInterface
{
    public void addBook(String bookID, String title, String author, String ISBN, String publicationDate, int quantity);
    public void updateBook(String bookID, String newTitle, String newAuthor, String newISBN, String newPublicationDate, boolean newAvailability, int newQuantity);
    public void deleteBook(String bookID);
    public void viewReport();
    void deleteRegisteredAccount(String userID);
    public void registerUser(String userID, String name, String contactInfo, String username, String password, String role);
}
