package org.example.Accounts;

public interface UserInterface {
    public void registerUser();

    public boolean login();

    public void logout();

    public void modifyUser(String userID, String newName);
}
