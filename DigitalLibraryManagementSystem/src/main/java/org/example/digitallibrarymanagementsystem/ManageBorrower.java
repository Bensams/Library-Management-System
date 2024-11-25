package org.example.digitallibrarymanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Accounts.Borrower;

public class ManageBorrower {
    private Borrower borrower;

    ManageBorrower(Borrower borrower) {
        this.borrower = borrower;
    }
}
