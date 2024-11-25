package org.example.Accounts;

import java.io.Serializable;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L; // Unique ID for versioning

    private String userID;
    private String name;
    private String contactInfo;
    private String username;
    private String password;
    private String role;

    // Constructor
    public User(String userID, String name, String contactInfo, String username, String password, String role) {
        this.userID = userID;
        this.name = name;
        this.contactInfo = contactInfo;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    //<editor-fold desc="ENCAPSULATION">
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    //</editor-fold>
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
