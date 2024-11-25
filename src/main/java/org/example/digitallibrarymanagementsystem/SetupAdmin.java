package org.example.digitallibrarymanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import org.example.Accounts.Admin;
import org.example.Accounts.User;
import org.example.Management.SerializedManagement;

import java.io.IOException;
import java.util.List;

public class SetupAdmin {
    SerializedManagement serializedManagement = SerializedManagement.getInstance();

    @FXML
    Label message_LBL;

    @FXML
    TextField name_TF;
    @FXML
    TextField username_TF;
    @FXML
    PasswordField password_PF;

    public SetupAdmin() throws IOException {
    }


    // Create Account on button click
    public void onCreateAccountBtnClick() {

        if (username_TF.getText().isEmpty() || name_TF.getText().isEmpty() || password_PF.getText().isEmpty()) {

            message_LBL.setTextFill(javafx.scene.paint.Color.RED);
            message_LBL.setStyle("-fx-font-weight: bold");
            message_LBL.setBackground(Background.fill(javafx.scene.paint.Color.WHITE));
            message_LBL.setText("Error: All fields must be filled out.");
            return;
        }

        // Create a new account
        System.out.println("Account created");
        Admin admin = new Admin(username_TF.getText(), name_TF.getText(), username_TF.getText(),"admin", password_PF.getText());
        serializedManagement.addUser(admin);

        // Load login.fxml and set it as the current scene
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene loginScene = new Scene(loginRoot);
            Stage primaryStage = (Stage) message_LBL.getScene().getWindow();
            primaryStage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
