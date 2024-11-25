// src/main/java/org/example/digitallibrarymanagementsystem/UpdateUserController.java
package org.example.digitallibrarymanagementsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Accounts.User;
import org.example.Management.UserManagement;

public class UpdateUserController {
    @FXML
    private TextField name_TF;
    @FXML
    private TextField contactInfo_TF;
    @FXML
    private TextField username_TF;
    @FXML
    private PasswordField password_PF;
    @FXML
    private PasswordField reconfirmPw_PF;
    @FXML
    private Label updateMSG_LBL;

    private User user;
    private UserManagement userManagement;
    private ManageLibrary manageLibrary;

    public void setUser(User user, UserManagement userManagement) {
        this.user = user;
        this.userManagement = userManagement;
        this.manageLibrary = manageLibrary;
        populateFields();
    }

    private void populateFields() {
        name_TF.setText(user.getName());
        contactInfo_TF.setText(user.getContactInfo());
        username_TF.setText(user.getUsername());
    }

    @FXML
    private void onSaveBtnClick() {
        if (name_TF.getText().isEmpty() || contactInfo_TF.getText().isEmpty() || username_TF.getText().isEmpty() || password_PF.getText().isEmpty()) {
            updateMSG_LBL.setText("Please fill in all fields.");
            return;
        }

        if (!password_PF.getText().equals(reconfirmPw_PF.getText())) {
            updateMSG_LBL.setText("Passwords do not match.");
            return;
        }

        userManagement.updateUserInfo( user.getUserID(),
            name_TF.getText(),
            contactInfo_TF.getText(),
            username_TF.getText(), password_PF.getText());
        closeWindow();
    }

    @FXML
    private void onCancelBtnClick() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) updateMSG_LBL.getScene().getWindow();
        stage.close();
    }
}