package org.example.digitallibrarymanagementsystem;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.Accounts.Admin;
import org.example.Accounts.Authentication;
import org.example.Accounts.Borrower;
import org.example.Accounts.User;
import org.example.Management.SerializedManagement;

import java.io.IOException;

public class LoginController {
    SerializedManagement serializedManagement = SerializedManagement.getInstance();
    Authentication auth = new Authentication(serializedManagement.getUsers());
    private int errorCount = 0;

    @FXML
    private Label signIn_LBL;
    @FXML
    private TextField username_ACTF;

    @FXML
    private PasswordField password_PF;

    @FXML
    private Label message_LBL;
    @FXML
    private ProgressBar loadingBar;

    private void manageLibrary(Admin admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageLibraryGUI/manageLibrary.fxml"));
            ManageLibrary manageLibrary = new ManageLibrary(admin);
            loader.setController(manageLibrary);
            Parent libraryRoot = loader.load();
            Scene libraryScene = new Scene(libraryRoot);
            Stage primaryStage = (Stage) signIn_LBL.getScene().getWindow();
            primaryStage.setScene(libraryScene);
            primaryStage.show(); // Ensure the stage is shown
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void manageBorrower(Borrower borrower) {
        try {
            Parent borrowerRoot = FXMLLoader.load(getClass().getResource("manageBorrower.fxml"));
            if (borrowerRoot == null) {
                throw new NullPointerException("manageBorrower.fxml not found");
            }
            Scene borrowerScene = new Scene(borrowerRoot);
            Stage primaryStage = (Stage) signIn_LBL.getScene().getWindow();
            if (primaryStage == null) {
                throw new NullPointerException("Primary stage not found");
            }
            primaryStage.setScene(borrowerScene);
            ManageBorrower manageBorrower = new ManageBorrower(borrower);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSignInButtonClick() {
        loadingBar.setVisible(true); // Show loading bar
        new Thread(() -> {
            User user = auth.login(username_ACTF.getText(), password_PF.getText());
            Platform.runLater(() -> {
                loadingBar.setVisible(false); // Hide loading bar
                if (user != null) {
                    if (user instanceof Admin) {
                        manageLibrary((Admin) user);
                    } else {
                        manageBorrower((Borrower) user);
                    }
                } else {
                    if (errorCount < 3) {
                        message_LBL.setText("Invalid credentials. Please try again.");
                        errorCount++;
                    } else {
                        message_LBL.setText("Forgot Password or Username? Contact admin!");
                    }
                }
            });
        }).start();
    }
}