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

import java.io.IOException;

public class ManageBorrower {
    private Borrower borrower;

    ManageBorrower(Borrower borrower) {
        this.borrower = borrower;
    }

//    @FXML
//    private void onProfileBtnClick() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateUserController.fxml"));
//            UpdateUserController updateUserController = new UpdateUserController();
//            updateUserController.setUser(borrower);
//            loader.setController(updateUserController);
//            Parent borrowerRoot = loader.load();
//            Scene borrowerScene = new Scene(borrowerRoot);
//            Stage primaryStage = (Stage) signIn_LBL.getScene().getWindow();
//            primaryStage.setScene(borrowerScene);
//            primaryStage.show(); // Ensure the stage is shown
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
